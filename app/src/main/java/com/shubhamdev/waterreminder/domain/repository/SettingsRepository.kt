package com.shubhamdev.waterreminder.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun onboardingShown(): Flow<Boolean>
    suspend fun setOnboardingShown(shown: Boolean)
    
    fun dailyGoalMl(): Flow<Int>
    suspend fun setDailyGoalMl(goal: Int)
    
    fun remindersEnabled(): Flow<Boolean>
    suspend fun setRemindersEnabled(enabled: Boolean)
    
    fun reminderIntervalMinutes(): Flow<Int>
    suspend fun setReminderIntervalMinutes(interval: Int)
    
    fun unitIsMl(): Flow<Boolean>
    suspend fun setUnitIsMl(isMl: Boolean)
}

