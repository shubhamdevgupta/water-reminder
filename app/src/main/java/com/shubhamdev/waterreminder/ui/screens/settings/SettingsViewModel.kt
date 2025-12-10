package com.shubhamdev.waterreminder.ui.screens.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shubhamdev.waterreminder.domain.usecase.GetSettingsUseCase
import com.shubhamdev.waterreminder.domain.usecase.SetSettingsUseCase
import com.shubhamdev.waterreminder.worker.WorkManagerScheduler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val getSettingsUseCase: GetSettingsUseCase,
    private val setSettingsUseCase: SetSettingsUseCase,
    private val context: Context? = null
) : ViewModel() {
    
    val dailyGoalMl: StateFlow<Int> = getSettingsUseCase.dailyGoalMl()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 2000
        )
    
    val remindersEnabled: StateFlow<Boolean> = getSettingsUseCase.remindersEnabled()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )
    
    val reminderIntervalMinutes: StateFlow<Int> = getSettingsUseCase.reminderIntervalMinutes()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 60
        )
    
    val unitIsMl: StateFlow<Boolean> = getSettingsUseCase.unitIsMl()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )
    
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()
    
    fun setDailyGoal(goal: Int) {
        viewModelScope.launch {
            try {
                setSettingsUseCase.setDailyGoalMl(goal)
                _uiState.update { it.copy(showSuccessMessage = true) }
                kotlinx.coroutines.delay(2000)
                _uiState.update { it.copy(showSuccessMessage = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message) }
            }
        }
    }
    
    fun setRemindersEnabled(enabled: Boolean) {
        viewModelScope.launch {
            try {
                setSettingsUseCase.setRemindersEnabled(enabled)
                // Schedule or cancel reminders
                context?.let {
                    val scheduler = WorkManagerScheduler(it)
                    if (enabled) {
                        val interval = reminderIntervalMinutes.value
                        scheduler.scheduleReminders(interval)
                    } else {
                        scheduler.cancelReminders()
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message) }
            }
        }
    }
    
    fun setReminderInterval(interval: Int) {
        viewModelScope.launch {
            try {
                setSettingsUseCase.setReminderIntervalMinutes(interval)
                // Reschedule reminders with new interval if enabled
                context?.let {
                    if (remindersEnabled.value) {
                        val scheduler = WorkManagerScheduler(it)
                        scheduler.scheduleReminders(interval)
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message) }
            }
        }
    }
    
    fun setUnitIsMl(isMl: Boolean) {
        viewModelScope.launch {
            try {
                setSettingsUseCase.setUnitIsMl(isMl)
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message) }
            }
        }
    }
    
    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}

data class SettingsUiState(
    val showSuccessMessage: Boolean = false,
    val errorMessage: String? = null
)

