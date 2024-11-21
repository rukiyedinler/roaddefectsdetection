package com.rukiyedinler.roaddefectsdetection.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.android.material.button.MaterialButton // MaterialButton içe aktarıldı
import com.rukiyedinler.roaddefectsdetection.R
import java.io.File

class HomeActivity : AppCompatActivity() {

    private lateinit var captureIV: ImageView
    private lateinit var imageUrl: Uri
    private lateinit var overlayText: TextView // TextView referansı

    // Fotoğraf çekme işlemi için contract
    private val contract = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        captureIV.setImageURI(null)
        captureIV.setImageURI(imageUrl)
        overlayText.visibility = View.GONE // Resim yüklendiğinde metni gizle
    }

    // Galeriden fotoğraf seçmek için contract
    private val pickImageContract = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            captureIV.setImageURI(it)
            overlayText.visibility = View.GONE // Galeriden fotoğraf seçildiğinde metni gizle
        }
    }

    // İzin isteği sonucunu işlemek için
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                imageUrl = createImageUri()
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
            imageUrl = createImageUri()
            captureIV = findViewById(R.id.captureImageView)
            overlayText = findViewById(R.id.overlayText) // TextView başlangıcı

            // MaterialButton referansı alınıyor
            val captureImgBtn = findViewById<MaterialButton>(R.id.captureImgBtn)
            captureImgBtn.setOnClickListener {
                contract.launch(imageUrl)
            }

            // MaterialButton referansı alınıyor
            val selectImgBtn = findViewById<MaterialButton>(R.id.selectImgBtn)
            selectImgBtn.setOnClickListener {
                pickImageContract.launch("image/*")
            }
        } else {
            // Kamera izni verilmemişse izin iste
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun createImageUri(): Uri {
        val image = File(filesDir, "camera_photos.png")
        return FileProvider.getUriForFile(this,
            "com.rukiyedinler.roaddefectsdetection.FileProvider",
            image)
    }
}