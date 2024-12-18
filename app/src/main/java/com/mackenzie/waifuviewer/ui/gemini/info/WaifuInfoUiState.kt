package com.mackenzie.waifuviewer.ui.gemini.info

sealed interface WaifuInfoUiState {
    data object Initial: WaifuInfoUiState
    data object Loading: WaifuInfoUiState
    data class Success(val outputText: String): WaifuInfoUiState
    data class Error(val errorMessage: String): WaifuInfoUiState
}