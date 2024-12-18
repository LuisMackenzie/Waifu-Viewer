package com.mackenzie.waifuviewer.ui.gpt

sealed interface WaifuGptUiState {
    data object Initial: WaifuGptUiState
    data object Loading: WaifuGptUiState
    data class Success(val outputText: String): WaifuGptUiState
    data class Error(val errorMessage: String): WaifuGptUiState
}