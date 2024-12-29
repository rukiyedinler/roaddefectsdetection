package com.rukiyedinler.roaddefectsdetection.repository

import android.util.Log
import com.google.gson.Gson
import com.rukiyedinler.roaddefectsdetection.data.ImageUploadBody
import com.rukiyedinler.roaddefectsdetection.data.LoginBody
import com.rukiyedinler.roaddefectsdetection.data.RegisterBody
import com.rukiyedinler.roaddefectsdetection.data.UserBody
import com.rukiyedinler.roaddefectsdetection.data.ValidateEmailBody
import com.rukiyedinler.roaddefectsdetection.utils.APIConsumer
import com.rukiyedinler.roaddefectsdetection.utils.RequestStatus
import com.rukiyedinler.roaddefectsdetection.utils.SimplifiedMessage
import kotlinx.coroutines.flow.flow

class AuthRepository(val consumer: APIConsumer) {

    fun validateEmailAddress(body: ValidateEmailBody) = flow {
        emit(RequestStatus.Waiting)
        val response = consumer.validateEmailAddress(body)
        if (response.isSuccessful) {
            emit(RequestStatus.Success(response.body()!!))
        } else {
            emit(
                RequestStatus.Error(
                    SimplifiedMessage.get(
                        response.errorBody()!!.byteStream().reader().readText()
                    )
                )
            )
        }
    }


    fun registerUser(body: RegisterBody) = flow {
        emit(RequestStatus.Waiting)
        val response = consumer.registerUser(body)
        if (response.isSuccessful) {
            emit(RequestStatus.Success(response.body()!!))
        } else {
            emit(
                RequestStatus.Error(
                    SimplifiedMessage.get(
                        response.errorBody()!!.byteStream().reader().readText()
                    )
                )
            )
        }
    }


    fun uploadImageBase64(token: String, body: ImageUploadBody) = flow {
        emit(RequestStatus.Waiting)

        val gson = Gson()
        val jsonString = gson.toJson(body)
        Log.d("ImageUploadBody", jsonString)

        val response = consumer.uploadImageBase64(token, body)

        if (response.isSuccessful) {
            emit(RequestStatus.Success(response.body()!!))
        } else {
            emit(
                RequestStatus.Error(
                    SimplifiedMessage.get(
                        response.errorBody()!!.byteStream().reader().readText()
                    )
                )
            )
        }
    }

    fun loginUser(body: LoginBody) = flow {
        emit(RequestStatus.Waiting)
        val response = consumer.loginUser(body)
        if (response.isSuccessful) {
            emit(RequestStatus.Success(response.body()!!))
        } else {
            emit(
                RequestStatus.Error(
                    SimplifiedMessage.get(
                        response.errorBody()!!.byteStream().reader().readText()
                    )
                )
            )
        }
    }

    fun getDefectList(body: UserBody) = flow {
        emit(RequestStatus.Waiting)
        val response = consumer.getDefectList(body)
        if (response.isSuccessful) {
            emit(RequestStatus.Success(response.body()!!))
        } else {
            emit(
                RequestStatus.Error(
                    SimplifiedMessage.get(
                        response.errorBody()!!.byteStream().reader().readText()
                    )
                )
            )
        }
    }
}