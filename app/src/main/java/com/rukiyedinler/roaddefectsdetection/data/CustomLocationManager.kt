package com.rukiyedinler.roaddefectsdetection.data

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.Locale

class CustomLocationManager(private val activity: Activity) {
    private var locationManager: LocationManager? = null
    private var currentLocation: Location? = null
    var latitude: Double = 0.0
    var longitude: Double = 0.0

    private val gpsLocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            currentLocation = location
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    private val networkLocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            currentLocation = location
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    fun isLocationPermissionGranted(): Boolean {
        val coarsePermission = ContextCompat.checkSelfPermission(
            activity, Manifest.permission.ACCESS_COARSE_LOCATION
        )
        val finePermission = ContextCompat.checkSelfPermission(
            activity, Manifest.permission.ACCESS_FINE_LOCATION
        )

        return if (coarsePermission != PackageManager.PERMISSION_GRANTED ||
            finePermission != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            false
        } else {
            true
        }
    }

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }

    fun getLocation(): String? {
        if (!isNetworkAvailable(activity)) {
            Toast.makeText(activity, "İnternet bağlantısı gerekli.", Toast.LENGTH_SHORT).show()
            return null
        }

        if (!isLocationPermissionGranted()) return null

        locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val hasGps = locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER) ?: false
        val hasNetwork =
            locationManager?.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ?: false

        if (!hasGps && !hasNetwork) {
            Toast.makeText(activity, "GPS veya Ağ sağlayıcı etkin değil.", Toast.LENGTH_SHORT)
                .show()
            return null
        }

        if (hasGps) {
            try {
                locationManager?.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    5000,
                    0F,
                    gpsLocationListener
                )
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
        }

        if (hasNetwork) {
            try {
                locationManager?.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    5000,
                    0F,
                    networkLocationListener
                )
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
        }

        val lastKnownGpsLocation: Location? = try {
            if (ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            } else {
                null
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
            null
        }

        val lastKnownNetworkLocation: Location? = try {
            if (ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                locationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            } else {
                null
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
            null
        }

        if (lastKnownGpsLocation != null && lastKnownNetworkLocation != null) {
            currentLocation =
                if (lastKnownGpsLocation.accuracy > lastKnownNetworkLocation.accuracy) {
                    lastKnownGpsLocation
                } else {
                    lastKnownNetworkLocation
                }
        } else if (lastKnownGpsLocation != null) {
            currentLocation = lastKnownGpsLocation
        } else if (lastKnownNetworkLocation != null) {
            currentLocation = lastKnownNetworkLocation
        }

        currentLocation?.let {
            latitude = it.latitude
            longitude = it.longitude
            Log.d("LocationInfo", "Latitude: $latitude, Longitude: $longitude")
            return getAddressFromLocation(it)
        }

        return "Konum bulunamadı."
    }

    private fun getAddressFromLocation(location: Location): String {
        val geocoder = Geocoder(activity, Locale.getDefault())
        return try {
            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            if (addresses != null && addresses.isNotEmpty()) {
                val address = addresses[0]
                val city = address.locality ?: "Unknown City"
                val district = address.subAdminArea ?: "Unknown District"
                val fullAddress = address.getAddressLine(0) ?: "Unknown Address"
                "City: $city, District: $district, Address: $fullAddress"
            } else {
                "Adres bulunamadı."
            }
        } catch (e: Exception) {
            e.printStackTrace()
            "Adres alınırken hata oluştu: ${e.message}"
        }
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo?.isConnected == true
    }

    fun stopLocationUpdates() {
        locationManager?.removeUpdates(gpsLocationListener)
        locationManager?.removeUpdates(networkLocationListener)
    }
}
