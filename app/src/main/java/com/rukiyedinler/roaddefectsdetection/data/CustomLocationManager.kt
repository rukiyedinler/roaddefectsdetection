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
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.Locale

class CustomLocationManager(private val activity: Activity) {
    private var locationManager: LocationManager? = null
    private var currentLocation: MutableLiveData<Location> = MutableLiveData()
    var latitude: Double = 0.0
    var longitude: Double = 0.0


    private val gpsLocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            currentLocation.value = location
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    init {

        locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val hasGps = locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER) ?: false
        val hasNetwork =
            locationManager?.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ?: false

        if (!hasGps && !hasNetwork) {
            Toast.makeText(activity, "GPS veya Ağ sağlayıcı etkin değil.", Toast.LENGTH_SHORT)
                .show()
        }

        if (hasGps) {
            try {
                val requestLocationUpdates = locationManager?.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    2000,
                    0F,
                    gpsLocationListener
                )
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
        }
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

        currentLocation.value?.let {
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
            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1,)
            if (addresses != null && addresses.isNotEmpty()) {
                val address = addresses[0]
                val fullAddress = address.getAddressLine(0) ?: "Adres getirilemedi."
                "$fullAddress"
            } else {
                "Adres bulunamadı."
            }
        } catch (e: Exception) {
            e.printStackTrace()
            "Adres alınırken hata oluştu: ${e.message}"
        }
    }

    fun stopLocationUpdates() {
        locationManager?.removeUpdates(gpsLocationListener)
    }
}
