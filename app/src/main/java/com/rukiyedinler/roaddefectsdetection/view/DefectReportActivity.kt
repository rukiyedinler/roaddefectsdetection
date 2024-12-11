package com.rukiyedinler.roaddefectsdetection.view

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.rukiyedinler.roaddefectsdetection.R
import java.io.File

class DefectReportActivity : AppCompatActivity() {

    private lateinit var captureIV: ImageView
    private lateinit var imageUrl: Uri
    private lateinit var overlayText: TextView
    private lateinit var imageFrame: View

    private lateinit var captureImgBtn: MaterialButton
    private lateinit var selectImgBtn: MaterialButton
    private lateinit var sendBtn: MaterialButton
    private lateinit var cancelBtn: MaterialButton
    private lateinit var infoText: TextView
    private lateinit var backBtn: MaterialButton
    private lateinit var infoCard: MaterialCardView

    private val contract = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            showImageUI()
            captureIV.setImageURI(imageUrl)
        }
    }

    private val pickImageContract = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            showImageUI()
            captureIV.setImageURI(it)
        }
    }

    private fun showImageUI() {
        imageFrame.visibility = View.VISIBLE
        overlayText.visibility = View.GONE

        captureImgBtn.visibility = View.GONE
        selectImgBtn.visibility = View.GONE

        sendBtn.visibility = View.VISIBLE
        cancelBtn.visibility = View.VISIBLE
    }

    private fun resetUI() {
        imageFrame.visibility = View.GONE
        overlayText.visibility = View.VISIBLE

        captureImgBtn.visibility = View.VISIBLE
        selectImgBtn.visibility = View.VISIBLE

        sendBtn.visibility = View.GONE
        cancelBtn.visibility = View.GONE

        infoText.visibility = View.GONE
        backBtn.visibility = View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_defect_report)

        captureIV = findViewById(R.id.captureImageView)
        overlayText = findViewById(R.id.overlayText)
        imageFrame = findViewById(R.id.imageFrame)

        captureImgBtn = findViewById(R.id.captureImgBtn)
        selectImgBtn = findViewById(R.id.selectImgBtn)
        sendBtn = findViewById(R.id.sendBtn)
        cancelBtn = findViewById(R.id.cancelBtn)
        infoText = findViewById(R.id.infoText)
        backBtn = findViewById(R.id.backBtn)
        infoCard = findViewById(R.id.infoCard)

        resetUI()



        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            imageUrl = createImageUri()

            captureImgBtn.setOnClickListener {
                contract.launch(imageUrl)
            }

            selectImgBtn.setOnClickListener {
                pickImageContract.launch("image/*")
            }
        }

        sendBtn.visibility = View.GONE
        cancelBtn.visibility = View.GONE

        captureImgBtn.setOnClickListener {
            contract.launch(imageUrl)
        }

        selectImgBtn.setOnClickListener {
            pickImageContract.launch("image/*")
        }

        sendBtn.setOnClickListener {
            // İlgili birime gönderildiğini belirten metni göster
            infoText.visibility = View.VISIBLE

            // Geri dönüş butonunu göster
            backBtn.visibility = View.VISIBLE

            // Gönder ve İptal butonlarını gizle
            sendBtn.visibility = View.GONE
            cancelBtn.visibility = View.GONE

            infoCard.setVisibility(View.VISIBLE); // Bilgi kartını görünür yapar

        }

        // Geri dönüş butonu işlemi
        backBtn.setOnClickListener {
            finish() // Bir önceki aktiviteye döner
        }


        cancelBtn.setOnClickListener {
            resetUI()
        }
    }

    private fun createImageUri(): Uri {
        val image = File(filesDir, "camera_photos.png")
        return FileProvider.getUriForFile(this,
            "com.rukiyedinler.roaddefectsdetection.FileProvider",
            image)
    }
}
