package com.rukiyedinler.roaddefectsdetection.view

import android.graphics.BitmapFactory
import com.rukiyedinler.roaddefectsdetection.R
import com.rukiyedinler.roaddefectsdetection.databinding.ActivityDefectDetailBinding
import android.os.Bundle
import android.util.Base64
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DefectDetailActivity: AppCompatActivity() {

    private lateinit var binding: ActivityDefectDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDefectDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val intent = this.intent
        if (intent != null) {
            val location = intent.getStringExtra("location")
            val imageBase64 = intent.getStringExtra("imageBase64")
            val createdAt = intent.getStringExtra("createdAt")
            val userName = intent.getStringExtra("userName")
            val confidence = intent.getFloatExtra("confidence",0f)

            val dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME
            val dateTime = LocalDateTime.parse(createdAt, dateTimeFormatter)

            val displayFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
            val formattedDate = dateTime.format(displayFormatter)


            binding.detailLocation.text = location
            binding.detailUserName.text = userName
            binding.detailCreatedAt.text = formattedDate

            binding.detailConfidence.text = confidence.toString()

            val imageBytes = Base64.decode(imageBase64, Base64.DEFAULT)
            val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            binding.detailImage.setImageBitmap(decodedImage)
        }
    }
}
