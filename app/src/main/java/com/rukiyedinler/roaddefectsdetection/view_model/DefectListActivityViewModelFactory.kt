package com.rukiyedinler.roaddefectsdetection.view_model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rukiyedinler.roaddefectsdetection.repository.AuthRepository
import java.security.InvalidParameterException

class DefectListActivityViewModelFactory(
    private val authRepository: AuthRepository,
    private val application: Application
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DefectListActivityViewModel::class.java)) {
            return DefectListActivityViewModel(authRepository, application) as T
        }
        throw InvalidParameterException("Unable to construct DefectReportViewModel")
    }
}
