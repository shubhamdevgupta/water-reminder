package com.shubhamdev.waterreminder.domain.usecase

import com.shubhamdev.waterreminder.domain.repository.WaterRepository

class AddWaterIntakeUseCase(
    private val waterRepository: WaterRepository
) {
    suspend operator fun invoke(amountMl: Int, note: String? = null): Long {
        return waterRepository.addIntake(amountMl, note)
    }
}

