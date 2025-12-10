package com.shubhamdev.waterreminder.domain.repository

import com.shubhamdev.waterreminder.domain.model.WaterIntake
import kotlinx.coroutines.flow.Flow

interface WaterRepository {
    suspend fun addIntake(amountMl: Int, note: String? = null): Long
    fun getIntakesForDay(dayStartMillis: Long, dayEndMillis: Long): Flow<List<WaterIntake>>
    fun getAllIntakes(): Flow<List<WaterIntake>>
    fun getTodayTotal(dayStartMillis: Long, dayEndMillis: Long): Flow<Int>
    suspend fun deleteAll()
}

