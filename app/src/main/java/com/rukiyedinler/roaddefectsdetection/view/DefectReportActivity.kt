package com.rukiyedinler.roaddefectsdetection.view

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.rukiyedinler.roaddefectsdetection.R
import com.rukiyedinler.roaddefectsdetection.data.CustomLocationManager
import com.rukiyedinler.roaddefectsdetection.data.ImageUploadBody
import com.rukiyedinler.roaddefectsdetection.data.User
import com.rukiyedinler.roaddefectsdetection.databinding.ActivityDefectReportBinding
import com.rukiyedinler.roaddefectsdetection.repository.AuthRepository
import com.rukiyedinler.roaddefectsdetection.utils.APIService
import com.rukiyedinler.roaddefectsdetection.utils.AuthToken
import com.rukiyedinler.roaddefectsdetection.utils.RequestStatus
import com.rukiyedinler.roaddefectsdetection.view_model.DefectReportActivityViewModel
import com.rukiyedinler.roaddefectsdetection.view_model.DefectReportViewModelFactory
import java.io.ByteArrayOutputStream
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

    private lateinit var mViewModel: DefectReportActivityViewModel
    private lateinit var binding: ActivityDefectReportBinding
    private lateinit var imageFile: File
    private lateinit var user: User
    private lateinit var locationHelper: CustomLocationManager

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
        locationHelper = CustomLocationManager(this)

        binding = ActivityDefectReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = intent.getParcelableExtra("user") ?: return

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

        mViewModel = ViewModelProvider(
            this, DefectReportViewModelFactory(
                AuthRepository(APIService.getService()),
                application
            )
        ).get(DefectReportActivityViewModel::class.java)
//        setupObserves()


        sendBtn.setOnClickListener {
            //Konum bilgisi alınıyor
            if (locationHelper.isLocationPermissionGranted()){
                val address = locationHelper.getLocation()
                address?.let {
                    Log.d("LocationInfo", it)
                } ?: run {
                    Toast.makeText(this, "Konum alınamadı!", Toast.LENGTH_SHORT).show()
                }

                //Resim konum database e kaydediliyor.
                val base64Image = imageViewToBase64(captureIV)
                if (base64Image != null) {
                    val token = AuthToken.getInstance(this).token
                    val imageBody = ImageUploadBody(
                        user.id,
                        base64Image,
                        address.toString()
                    )
                    mViewModel.uploadImageBase64(token!!, imageBody)
                    mViewModel.imageUploadStatus.observe(this) { status ->
                        when (status) {
                            is RequestStatus.Waiting -> {
                                Toast.makeText(this, "Yükleniyor...", Toast.LENGTH_SHORT).show()
                            }
                            is RequestStatus.Success -> {
                                infoText.visibility = View.VISIBLE

                                // Geri dönüş butonunu göster
                                backBtn.visibility = View.VISIBLE

                                // Gönder ve İptal butonlarını gizle
                                sendBtn.visibility = View.GONE
                                cancelBtn.visibility = View.GONE

                                infoCard.setVisibility(View.VISIBLE); // Bilgi kartını görünür yapar


                            }
                            is RequestStatus.Error -> {
                                Toast.makeText(this, "Hata: ${status.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                } else {
                    Toast.makeText(this, "Resim seçilmedi!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        backBtn.setOnClickListener {
            finish() // Bir önceki aktiviteye döner
        }


        cancelBtn.setOnClickListener {
            resetUI()
        }


    }

    fun imageViewToBase64(imageView: ImageView): String? {
        // ImageView'deki Drawable'ı Bitmap'e dönüştür
        val drawable = imageView.drawable as? BitmapDrawable ?: return null
        val bitmap = drawable.bitmap

        // Bitmap'i ByteArray'e dönüştür
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()

        // ByteArray'i Base64 string'e dönüştürür
        return Base64.encodeToString(byteArray, Base64.NO_WRAP)
    }

    private fun createImageFile(): File {
        return File(filesDir, "camera_image_${System.currentTimeMillis()}.png")
    }

    private fun createImageUri(): Uri {
        val image = File(filesDir, "camera_photos.png")
        return FileProvider.getUriForFile(
            this,
            "com.rukiyedinler.roaddefectsdetection.FileProvider",
            image
        )
    }
}
