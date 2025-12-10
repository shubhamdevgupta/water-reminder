package com.shubhamdev.waterreminder.data.datastore

import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.booleanPreferencesKey

object PreferenceKeys {
    val ONBOARDING_SHOWN = booleanPreferencesKey("onboarding_shown")
    val DAILY_GOAL_ML = intPreferencesKey("daily_goal_ml")
    val REMINDERS_ENABLED = booleanPreferencesKey("reminders_enabled")
    val REMINDER_INTERVAL_MINUTES = intPreferencesKey("reminder_interval_minutes")
    val UNIT_IS_ML = booleanPreferencesKey("unit_is_ml")
}

