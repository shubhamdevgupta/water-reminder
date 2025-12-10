package com.shubhamdev.waterreminder.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "water_intakes")
data class WaterIntakeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val amountMl: Int,
    val timestamp: Long,
    val note: String? = null
)

