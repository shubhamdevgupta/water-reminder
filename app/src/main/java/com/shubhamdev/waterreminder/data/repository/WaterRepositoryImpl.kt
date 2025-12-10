package com.shubhamdev.waterreminder.data.repository

import com.shubhamdev.waterreminder.data.local.WaterIntakeDao
import com.shubhamdev.waterreminder.data.model.toDomain
import com.shubhamdev.waterreminder.data.model.toEntity
import com.shubhamdev.waterreminder.domain.model.WaterIntake
import com.shubhamdev.waterreminder.domain.repository.WaterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WaterRepositoryImpl(
    private val waterIntakeDao: WaterIntakeDao
) : WaterRepository {
    
    override suspend fun addIntake(amountMl: Int, note: String?): Long {
        val entity = WaterIntake(
            amountMl = amountMl,
            timestamp = System.currentTimeMillis(),
            note = note
        ).toEntity()
        return waterIntakeDao.insert(entity)
    }
    
    override fun getIntakesForDay(dayStartMillis: Long, dayEndMillis: Long): Flow<List<WaterIntake>> {
        return waterIntakeDao.getIntakesForDay(dayStartMillis, dayEndMillis)
            .map { entities -> entities.map { it.toDomain() } }
    }
    
    override fun getAllIntakes(): Flow<List<WaterIntake>> {
        return waterIntakeDao.getAllIntakes()
            .map { entities -> entities.map { it.toDomain() } }
    }
    
    override fun getTodayTotal(dayStartMillis: Long, dayEndMillis: Long): Flow<Int> {
        return waterIntakeDao.getTotalForDay(dayStartMillis, dayEndMillis)
    }
    
    override suspend fun deleteAll() {
        waterIntakeDao.deleteAll()
    }
}

