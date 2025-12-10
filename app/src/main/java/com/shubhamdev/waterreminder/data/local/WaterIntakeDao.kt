package com.shubhamdev.waterreminder.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WaterIntakeDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(waterIntake: WaterIntakeEntity): Long
    
    @Query("SELECT * FROM water_intakes WHERE timestamp >= :dayStartMillis AND timestamp < :dayEndMillis ORDER BY timestamp DESC")
    fun getIntakesForDay(dayStartMillis: Long, dayEndMillis: Long): Flow<List<WaterIntakeEntity>>
    
    @Query("SELECT * FROM water_intakes ORDER BY timestamp DESC")
    fun getAllIntakes(): Flow<List<WaterIntakeEntity>>
    
    @Query("DELETE FROM water_intakes")
    suspend fun deleteAll()
    
    @Query("SELECT COALESCE(SUM(amountMl), 0) FROM water_intakes WHERE timestamp >= :dayStartMillis AND timestamp < :dayEndMillis")
    fun getTotalForDay(dayStartMillis: Long, dayEndMillis: Long): Flow<Int>
}

