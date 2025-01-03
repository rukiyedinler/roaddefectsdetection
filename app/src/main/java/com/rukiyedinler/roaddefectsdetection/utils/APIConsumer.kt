package com.rukiyedinler.roaddefectsdetection.utils

import androidx.lifecycle.viewmodel.CreationExtras
import com.rukiyedinler.roaddefectsdetection.data.RegisterBody
import com.rukiyedinler.roaddefectsdetection.data.AuthResponse
import com.rukiyedinler.roaddefectsdetection.data.ImageUploadBody
import com.rukiyedinler.roaddefectsdetection.data.ImageUploadResponse
import com.rukiyedinler.roaddefectsdetection.data.LoginBody
import com.rukiyedinler.roaddefectsdetection.data.Pothole
import com.rukiyedinler.roaddefectsdetection.data.UniqueEmailValidationResponse
import com.rukiyedinler.roaddefectsdetection.data.User
import com.rukiyedinler.roaddefectsdetection.data.UserBody
import com.rukiyedinler.roaddefectsdetection.data.ValidateEmailBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface APIConsumer {
    @POST("validate-unique-email")
    suspend fun validateEmailAddress(@Body body: ValidateEmailBody): Response<UniqueEmailValidationResponse>

    @POST("registerUser")
    suspend fun registerUser(@Body body: RegisterBody): Response<AuthResponse>

    @POST("login")
    suspend fun loginUser(@Body body: LoginBody): Response<AuthResponse>

    @POST("getDefectList")
    suspend fun getDefectList(@Body body: UserBody): Response<List<Pothole>>

    @POST("uploadImageBase64")
    suspend fun uploadImageBase64(
        @Header("Authorization") token: String,
        @Body requestBody: ImageUploadBody
    ): Response<ImageUploadResponse>


}