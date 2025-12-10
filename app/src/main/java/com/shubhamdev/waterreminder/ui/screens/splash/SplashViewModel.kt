package com.shubhamdev.waterreminder.ui.screens.splash



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shubhamdev.waterreminder.domain.repository.OnboardingRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class SplashViewModel(
    private val onboardingRepository: OnboardingRepository
) : ViewModel() {
    // Expose whether onboarding was shown
    val onboardingShown: StateFlow<Boolean> = onboardingRepository
        .isOnboardingShown()
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)
}
