package com.shubhamdev.waterreminder.data.local

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [WaterIntakeEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun waterIntakeDao(): WaterIntakeDao
    
    companion object {
        const val DATABASE_NAME = "water_reminder_db"
    }
}

