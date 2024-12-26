package com.rukiyedinler.roaddefectsdetection.data

import com.google.gson.annotations.SerializedName


data class Pothole(@SerializedName("_id") val id: String,  val description: String,  val imageBase64: String,val createdAt: String,val userId: String, val location: String, val confidence: Float, val userName: String )
