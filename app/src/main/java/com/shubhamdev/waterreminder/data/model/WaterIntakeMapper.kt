package com.shubhamdev.waterreminder.data.model

import com.shubhamdev.waterreminder.data.local.WaterIntakeEntity
import com.shubhamdev.waterreminder.domain.model.WaterIntake

fun WaterIntakeEntity.toDomain(): WaterIntake {
    return WaterIntake(
        id = id,
        amountMl = amountMl,
        timestamp = timestamp,
        note = note
    )
}

fun WaterIntake.toEntity(): WaterIntakeEntity {
    return WaterIntakeEntity(
        id = id,
        amountMl = amountMl,
        timestamp = timestamp,
        note = note
    )
}

