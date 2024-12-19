package com.rukiyedinler.roaddefectsdetection.view_model

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rukiyedinler.roaddefectsdetection.data.ImageUploadBody
import com.rukiyedinler.roaddefectsdetection.data.ImageUploadResponse
import com.rukiyedinler.roaddefectsdetection.repository.AuthRepository
import com.rukiyedinler.roaddefectsdetection.utils.RequestStatus
import kotlinx.coroutines.launch

class DefectReportActivityViewModel(
    private val authRepository: AuthRepository,
    private val application: Application
) : ViewModel() {

    private val _imageUploadStatus = MutableLiveData<RequestStatus<ImageUploadResponse>>()
    val imageUploadStatus: LiveData<RequestStatus<ImageUploadResponse>> get() = _imageUploadStatus

    fun uploadImageBase64(token: String, body: ImageUploadBody) {
        viewModelScope.launch {
            authRepository.uploadImageBase64(token, body).collect {
                _imageUploadStatus.value = it
            }
        }
    }
}
