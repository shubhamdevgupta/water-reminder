package com.shubhamdev.waterreminder.ui.screens.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shubhamdev.waterreminder.domain.repository.WaterRepository

class HistoryViewModelFactory(
    private val waterRepository: WaterRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            return HistoryViewModel(waterRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

