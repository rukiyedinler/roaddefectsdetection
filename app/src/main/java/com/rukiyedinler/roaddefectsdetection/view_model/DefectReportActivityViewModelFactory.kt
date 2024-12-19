package com.rukiyedinler.roaddefectsdetection.view_model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rukiyedinler.roaddefectsdetection.repository.AuthRepository
import java.security.InvalidParameterException

class DefectReportViewModelFactory(
    private val authRepository: AuthRepository,
    private val application: Application
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DefectReportActivityViewModel::class.java)) {
            return DefectReportActivityViewModel(authRepository, application) as T
        }
        throw InvalidParameterException("Unable to construct DefectReportViewModel")
    }
}
