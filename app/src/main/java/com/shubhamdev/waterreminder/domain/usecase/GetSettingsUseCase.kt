package com.shubhamdev.waterreminder.domain.usecase

import com.shubhamdev.waterreminder.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow

class GetSettingsUseCase(
    private val settingsRepository: SettingsRepository
) {
    fun dailyGoalMl(): Flow<Int> = settingsRepository.dailyGoalMl()
    fun remindersEnabled(): Flow<Boolean> = settingsRepository.remindersEnabled()
    fun reminderIntervalMinutes(): Flow<Int> = settingsRepository.reminderIntervalMinutes()
    fun unitIsMl(): Flow<Boolean> = settingsRepository.unitIsMl()
}

