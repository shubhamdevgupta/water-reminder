package com.shubhamdev.waterreminder.domain.usecase

import com.shubhamdev.waterreminder.domain.repository.SettingsRepository

class SetSettingsUseCase(
    private val settingsRepository: SettingsRepository
) {
    suspend fun setDailyGoalMl(goal: Int) {
        settingsRepository.setDailyGoalMl(goal)
    }
    
    suspend fun setRemindersEnabled(enabled: Boolean) {
        settingsRepository.setRemindersEnabled(enabled)
    }
    
    suspend fun setReminderIntervalMinutes(interval: Int) {
        settingsRepository.setReminderIntervalMinutes(interval)
    }
    
    suspend fun setUnitIsMl(isMl: Boolean) {
        settingsRepository.setUnitIsMl(isMl)
    }
}

