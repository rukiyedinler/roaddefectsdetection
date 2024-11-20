package com.rukiyedinler.roaddefectsdetection.view
import android.Manifest
import android.content.pm.PackageManager


import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.rukiyedinler.roaddefectsdetection.R
import java.io.File
import androidx.core.content.ContextCompat

class HomeActivity : AppCompatActivity() {
    private lateinit var captureIV : ImageView
    private lateinit var imageUrl : Uri

    private val contract = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        captureIV.setImageURI(null)
        captureIV.setImageURI(imageUrl)
    }

    // İzin isteği sonucunu işlemek için
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // İzin verildiğinde kamera açılır
                contract.launch(imageUrl)
            } else {
                // İzin verilmediğinde kullanıcıya bilgi verilebilir
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // İzin kontrolü
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Kamera izni verilmişse, işlemlere devam et
            imageUrl = createImageUri()
            captureIV = findViewById(R.id.captureImageView)
            val captureImgBtn = findViewById<Button>(R.id.captureImgBtn)
            captureImgBtn.setOnClickListener {
                contract.launch(imageUrl)
            }
        } else {
            // Kamera izni verilmemişse izin iste
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun createImageUri():Uri {
        val image = File(filesDir,"camera_photos.png")
        return FileProvider.getUriForFile(this,
            "com.rukiyedinler.roaddefectsdetection.FileProvider",
            image)
    }
}