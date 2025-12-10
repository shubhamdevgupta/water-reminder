package com.shubhamdev.waterreminder.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferencesDataSource(private val dataStore: DataStore<Preferences>) {

    val onboardingShown: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[PreferenceKeys.ONBOARDING_SHOWN] ?: false
    }

    val dailyGoalMl: Flow<Int> = dataStore.data.map { prefs ->
        prefs[PreferenceKeys.DAILY_GOAL_ML] ?: 2000
    }

    val remindersEnabled: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[PreferenceKeys.REMINDERS_ENABLED] ?: true
    }

    val reminderIntervalMinutes: Flow<Int> = dataStore.data.map { prefs ->
        prefs[PreferenceKeys.REMINDER_INTERVAL_MINUTES] ?: 60
    }

    val unitIsMl: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[PreferenceKeys.UNIT_IS_ML] ?: true
    }

    suspend fun setOnboardingShown(shown: Boolean) {
        dataStore.edit { prefs ->
            prefs[PreferenceKeys.ONBOARDING_SHOWN] = shown
        }
    }

    suspend fun setDailyGoalMl(goal: Int) {
        dataStore.edit { prefs ->
            prefs[PreferenceKeys.DAILY_GOAL_ML] = goal
        }
    }

    suspend fun setRemindersEnabled(enabled: Boolean) {
        dataStore.edit { prefs ->
            prefs[PreferenceKeys.REMINDERS_ENABLED] = enabled
        }
    }

    suspend fun setReminderIntervalMinutes(interval: Int) {
        dataStore.edit { prefs ->
            prefs[PreferenceKeys.REMINDER_INTERVAL_MINUTES] = interval
        }
    }

    suspend fun setUnitIsMl(isMl: Boolean) {
        dataStore.edit { prefs ->
            prefs[PreferenceKeys.UNIT_IS_ML] = isMl
        }
    }
}

