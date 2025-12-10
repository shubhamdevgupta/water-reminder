package com.shubhamdev.waterreminder.ui.screens.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shubhamdev.waterreminder.domain.model.WaterIntake
import com.shubhamdev.waterreminder.domain.repository.WaterRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar

class HistoryViewModel(
    private val waterRepository: WaterRepository
) : ViewModel() {
    
    val allIntakes: StateFlow<List<WaterIntake>> = waterRepository.getAllIntakes()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()
    
    fun deleteAll() {
        viewModelScope.launch {
            try {
                waterRepository.deleteAll()
                _uiState.update { it.copy(showDeleteConfirmation = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message) }
            }
        }
    }
    
    fun showDeleteConfirmation() {
        _uiState.update { it.copy(showDeleteConfirmation = true) }
    }
    
    fun dismissDeleteConfirmation() {
        _uiState.update { it.copy(showDeleteConfirmation = false) }
    }
    
    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
    
    fun getIntakesGroupedByDate(): Map<String, List<WaterIntake>> {
        val calendar = Calendar.getInstance()
        return allIntakes.value.groupBy { intake ->
            calendar.timeInMillis = intake.timestamp
            val dateStr = "${calendar.get(Calendar.YEAR)}-${calendar.get(Calendar.MONTH) + 1}-${calendar.get(Calendar.DAY_OF_MONTH)}"
            dateStr
        }
    }
}

data class HistoryUiState(
    val showDeleteConfirmation: Boolean = false,
    val errorMessage: String? = null
)

