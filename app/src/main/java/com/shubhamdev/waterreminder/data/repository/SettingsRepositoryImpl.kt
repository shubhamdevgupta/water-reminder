package com.shubhamdev.waterreminder.data.repository

import com.shubhamdev.waterreminder.data.datastore.PreferencesDataSource
import com.shubhamdev.waterreminder.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow

class SettingsRepositoryImpl(
    private val preferencesDataSource: PreferencesDataSource
) : SettingsRepository {
    
    override fun onboardingShown(): Flow<Boolean> {
        return preferencesDataSource.onboardingShown
    }
    
    override suspend fun setOnboardingShown(shown: Boolean) {
        preferencesDataSource.setOnboardingShown(shown)
    }
    
    override fun dailyGoalMl(): Flow<Int> {
        return preferencesDataSource.dailyGoalMl
    }
    
    override suspend fun setDailyGoalMl(goal: Int) {
        preferencesDataSource.setDailyGoalMl(goal)
    }
    
    override fun remindersEnabled(): Flow<Boolean> {
        return preferencesDataSource.remindersEnabled
    }
    
    override suspend fun setRemindersEnabled(enabled: Boolean) {
        preferencesDataSource.setRemindersEnabled(enabled)
    }
    
    override fun reminderIntervalMinutes(): Flow<Int> {
        return preferencesDataSource.reminderIntervalMinutes
    }
    
    override suspend fun setReminderIntervalMinutes(interval: Int) {
        preferencesDataSource.setReminderIntervalMinutes(interval)
    }
    
    override fun unitIsMl(): Flow<Boolean> {
        return preferencesDataSource.unitIsMl
    }
    
    override suspend fun setUnitIsMl(isMl: Boolean) {
        preferencesDataSource.setUnitIsMl(isMl)
    }
}

