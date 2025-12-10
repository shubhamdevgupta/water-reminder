package com.shubhamdev.waterreminder.domain.usecase

import com.shubhamdev.waterreminder.domain.repository.WaterRepository
import kotlinx.coroutines.flow.Flow

class GetTodayTotalUseCase(
    private val waterRepository: WaterRepository
) {
    operator fun invoke(dayStartMillis: Long, dayEndMillis: Long): Flow<Int> {
        return waterRepository.getTodayTotal(dayStartMillis, dayEndMillis)
    }
}

