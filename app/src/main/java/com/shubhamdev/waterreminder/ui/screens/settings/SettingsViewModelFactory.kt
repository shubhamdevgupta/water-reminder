package com.shubhamdev.waterreminder.ui.screens.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shubhamdev.waterreminder.domain.usecase.GetSettingsUseCase
import com.shubhamdev.waterreminder.domain.usecase.SetSettingsUseCase

class SettingsViewModelFactory(
    private val getSettingsUseCase: GetSettingsUseCase,
    private val setSettingsUseCase: SetSettingsUseCase,
    private val context: Context
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(getSettingsUseCase, setSettingsUseCase, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

