package com.shubhamdev.waterreminder.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shubhamdev.waterreminder.domain.usecase.AddWaterIntakeUseCase
import com.shubhamdev.waterreminder.domain.usecase.GetTodayIntakesUseCase
import com.shubhamdev.waterreminder.domain.usecase.GetTodayTotalUseCase
import com.shubhamdev.waterreminder.domain.usecase.GetSettingsUseCase

class DashboardViewModelFactory(
    private val addWaterIntakeUseCase: AddWaterIntakeUseCase,
    private val getTodayTotalUseCase: GetTodayTotalUseCase,
    private val getTodayIntakesUseCase: GetTodayIntakesUseCase,
    private val getSettingsUseCase: GetSettingsUseCase
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            return DashboardViewModel(
                addWaterIntakeUseCase,
                getTodayTotalUseCase,
                getTodayIntakesUseCase,
                getSettingsUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

