package com.shubhamdev.waterreminder.domain.model

data class WaterIntake(
    val id: Long = 0,
    val amountMl: Int,
    val timestamp: Long,
    val note: String? = null
)

