package com.shubhamdev.waterreminder.ui.screens.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shubhamdev.waterreminder.domain.repository.OnboardingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val onboardingRepository: OnboardingRepository
) : ViewModel() {

    // UI state
    private val _currentPage = MutableStateFlow(0)
    val currentPage: StateFlow<Int> = _currentPage

    fun setCurrentPage(index: Int) {
        _currentPage.value = index
    }

    fun markOnboardingComplete(onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            onboardingRepository.setOnboardingShown(true)
            onComplete()
        }
    }
}
