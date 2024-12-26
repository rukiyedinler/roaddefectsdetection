package com.rukiyedinler.roaddefectsdetection.view_model

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rukiyedinler.roaddefectsdetection.data.ImageUploadBody
import com.rukiyedinler.roaddefectsdetection.data.ImageUploadResponse
import com.rukiyedinler.roaddefectsdetection.data.Pothole
import com.rukiyedinler.roaddefectsdetection.data.UserBody
import com.rukiyedinler.roaddefectsdetection.repository.AuthRepository
import com.rukiyedinler.roaddefectsdetection.utils.RequestStatus
import kotlinx.coroutines.launch

class DefectListActivityViewModel(
    private val authRepository: AuthRepository,
    private val application: Application
) : ViewModel()  {
        private val _potholeList = MutableLiveData<RequestStatus<List<Pothole>>>()
        val potholeList: MutableLiveData<RequestStatus<List<Pothole>>> get() = _potholeList
        fun getPotholeList(): LiveData<RequestStatus<List<Pothole>>> = potholeList

        fun getDefectList(body: UserBody) {
            viewModelScope.launch {
                authRepository.getDefectList(body).collect {
                    _potholeList.value = it
                }
            }
        }
    }

