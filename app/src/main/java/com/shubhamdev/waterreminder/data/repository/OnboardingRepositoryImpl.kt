package com.shubhamdev.waterreminder.data.repository


import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.shubhamdev.waterreminder.domain.repository.OnboardingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OnboardingRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : OnboardingRepository {

    private object Keys {
        val ONBOARDING_SHOWN = booleanPreferencesKey("onboarding_shown")
    }

    override fun isOnboardingShown(): Flow<Boolean> {
        return dataStore.data
            .map { prefs -> prefs[Keys.ONBOARDING_SHOWN] ?: false }
    }

    override suspend fun setOnboardingShown(shown: Boolean) {
        dataStore.edit { prefs ->
            prefs[Keys.ONBOARDING_SHOWN] = shown
        }
    }
}
