package com.shubhamdev.waterreminder.data.datastore

// DataStoreExt.kt

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

// Name of DataStore file
private const val PREFERENCES_NAME = "water_reminder_prefs"

// delegate
val Context.dataStore by preferencesDataStore(name = PREFERENCES_NAME)
