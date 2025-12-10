package com.shubhamdev.waterreminder.domain.usecase

import com.shubhamdev.waterreminder.domain.model.WaterIntake
import com.shubhamdev.waterreminder.domain.repository.WaterRepository
import kotlinx.coroutines.flow.Flow

class GetTodayIntakesUseCase(
    private val waterRepository: WaterRepository
) {
    operator fun invoke(dayStartMillis: Long, dayEndMillis: Long): Flow<List<WaterIntake>> {
        return waterRepository.getIntakesForDay(dayStartMillis, dayEndMillis)
    }
}

