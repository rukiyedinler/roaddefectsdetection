package com.rukiyedinler.roaddefectsdetection.view_model

import android.app.Application
import android.util.Base64
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rukiyedinler.roaddefectsdetection.data.LoginBody
import com.rukiyedinler.roaddefectsdetection.data.RegisterBody
import com.rukiyedinler.roaddefectsdetection.data.User
import com.rukiyedinler.roaddefectsdetection.data.UserBody
import com.rukiyedinler.roaddefectsdetection.data.ValidateEmailBody
import com.rukiyedinler.roaddefectsdetection.repository.AuthRepository
import com.rukiyedinler.roaddefectsdetection.utils.AuthToken
import com.rukiyedinler.roaddefectsdetection.utils.RequestStatus
import kotlinx.coroutines.launch

class LoginActivityViewModel(val authRepository: AuthRepository, val application: Application) :
    ViewModel() {
    private var isLoading: MutableLiveData<Boolean> =
        MutableLiveData<Boolean>().apply { value = false }
    private var errorMessage: MutableLiveData<HashMap<String, String>> = MutableLiveData()
    private var user: MutableLiveData<User> = MutableLiveData()

    fun getIsLoading(): LiveData<Boolean> = isLoading
    fun getErrorMessage(): LiveData<HashMap<String, String>> = errorMessage
    fun getUser(): LiveData<User> = user

    fun loginUser(body: LoginBody) {
        viewModelScope.launch {
            authRepository.loginUser(body).collect {
                when (it) {
                    is RequestStatus.Waiting -> {
                        isLoading.value = true
                    }
                    is RequestStatus.Success -> {
                        isLoading.value = false

                        // Yeni token ve role güncellemesi
                        val authTokenInstance = AuthToken.getInstance(application.baseContext)
                        authTokenInstance.token = it.data.accessToken // Yeni token kaydediliyor
                        authTokenInstance.role = null // Eski role temizleniyor (gerekirse)

                        // Güncellenmiş kullanıcı bilgisiyle role belirleme
                        val updatedUser = it.data.user.apply {
                            role = when (AuthToken.getInstance(application.baseContext).role) {
                                "Admin" -> 1
                                "User" -> 2
                                else -> 0
                            }
                        }
                        println("Updated User Role: ${updatedUser.role}")
                        user.postValue(updatedUser)
                        AuthToken.getInstance(application.baseContext).token = it.data.accessToken
                    }
                    is RequestStatus.Error -> {
                        isLoading.value = false
                        errorMessage.value = it.message
                    }
                }
            }
        }
    }
}