package com.rukiyedinler.roaddefectsdetection.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(@SerializedName("_id") val id: String, val fullName: String, val email: String, var role: Int) :
    Parcelable
