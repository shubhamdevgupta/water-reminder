package com.shubhamdev.waterreminder.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shubhamdev.waterreminder.domain.model.WaterIntake
import com.shubhamdev.waterreminder.domain.usecase.AddWaterIntakeUseCase
import com.shubhamdev.waterreminder.domain.usecase.GetTodayIntakesUseCase
import com.shubhamdev.waterreminder.domain.usecase.GetTodayTotalUseCase
import com.shubhamdev.waterreminder.domain.usecase.GetSettingsUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar

class DashboardViewModel(
    private val addWaterIntakeUseCase: AddWaterIntakeUseCase,
    private val getTodayTotalUseCase: GetTodayTotalUseCase,
    private val getTodayIntakesUseCase: GetTodayIntakesUseCase,
    private val getSettingsUseCase: GetSettingsUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()
    
    private val dayStartMillis: Long
        get() {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            return calendar.timeInMillis
        }
    
    private val dayEndMillis: Long
        get() {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, 23)
            calendar.set(Calendar.MINUTE, 59)
            calendar.set(Calendar.SECOND, 59)
            calendar.set(Calendar.MILLISECOND, 999)
            return calendar.timeInMillis
        }
    
    init {
        loadData()
    }
    
    private fun loadData() {
        viewModelScope.launch {
            combine(
                getTodayTotalUseCase(dayStartMillis, dayEndMillis),
                getSettingsUseCase.dailyGoalMl()
            ) { todayTotal, dailyGoal ->
                val progress = if (dailyGoal > 0) {
                    (todayTotal.toFloat() / dailyGoal).coerceIn(0f, 1f)
                } else {
                    0f
                }
                _uiState.update {
                    it.copy(
                        todayTotalMl = todayTotal,
                        dailyGoalMl = dailyGoal,
                        progressPercent = progress
                    )
                }
            }.collect()
        }
    }
    
    fun quickAdd(amount: Int) {
        viewModelScope.launch {
            try {
                addWaterIntakeUseCase(amount)
                _uiState.update { it.copy(showSuccess = true) }
                // Reset success message after a delay
                kotlinx.coroutines.delay(2000)
                _uiState.update { it.copy(showSuccess = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message) }
            }
        }
    }
    
    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
    
    val todayIntakes: Flow<List<WaterIntake>> = getTodayIntakesUseCase(dayStartMillis, dayEndMillis)
}

data class DashboardUiState(
    val todayTotalMl: Int = 0,
    val dailyGoalMl: Int = 2000,
    val progressPercent: Float = 0f,
    val showSuccess: Boolean = false,
    val errorMessage: String? = null
)

