package com.rukiyedinler.roaddefectsdetection.utils

import com.rukiyedinler.roaddefectsdetection.data.RegisterBody
import com.rukiyedinler.roaddefectsdetection.data.AuthResponse
import com.rukiyedinler.roaddefectsdetection.data.LoginBody
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

    @POST("getUser")
    suspend fun getUser(@Header("Authorization") token: String): Response<User>


}