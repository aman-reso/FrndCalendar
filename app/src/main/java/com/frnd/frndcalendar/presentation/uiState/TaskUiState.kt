package com.frnd.frndcalendar.presentation.uiState

import com.frnd.frndcalendar.remote.model.TaskResponse

sealed interface TaskUiState {
    val isLoading: Boolean

    data class Error(override val isLoading: Boolean, val errorMessage: String) : TaskUiState
    data class Empty(override val isLoading: Boolean) : TaskUiState
    data class Success(val data: List<TaskResponse>, override val isLoading: Boolean) : TaskUiState
}