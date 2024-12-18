package com.mackenzie.waifuviewer.ui.gemini.resume

sealed interface SummarizeWaifuUiState {
    data object Initial: SummarizeWaifuUiState
    data object Loading: SummarizeWaifuUiState
    data class Success(val outputText: String): SummarizeWaifuUiState
    data class Error(val errorMessage: String): SummarizeWaifuUiState
}