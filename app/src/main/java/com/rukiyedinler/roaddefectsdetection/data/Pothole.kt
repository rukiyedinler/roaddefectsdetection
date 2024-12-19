package com.rukiyedinler.roaddefectsdetection.data

import com.google.gson.annotations.SerializedName


data class Pothole(@SerializedName("_id") val id: String, val base64Image: String)
