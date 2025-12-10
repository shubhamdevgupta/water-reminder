package com.shubhamdev.waterreminder.domain.usecase

import com.shubhamdev.waterreminder.domain.model.WaterIntake
import com.shubhamdev.waterreminder.domain.repository.WaterRepository
import kotlinx.coroutines.flow.Flow

class GetAllIntakesUseCase(
    private val waterRepository: WaterRepository
) {
    operator fun invoke(): Flow<List<WaterIntake>> {
        return waterRepository.getAllIntakes()
    }
}

