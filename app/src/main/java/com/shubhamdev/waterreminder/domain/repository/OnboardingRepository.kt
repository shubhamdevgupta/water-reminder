package com.shubhamdev.waterreminder.domain.repository


import kotlinx.coroutines.flow.Flow

interface OnboardingRepository {
    fun isOnboardingShown(): Flow<Boolean>
    suspend fun setOnboardingShown(shown: Boolean)
}