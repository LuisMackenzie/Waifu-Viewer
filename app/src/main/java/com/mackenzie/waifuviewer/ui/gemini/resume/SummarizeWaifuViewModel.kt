package com.mackenzie.waifuviewer.ui.gemini.resume

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SummarizeWaifuViewModel(private val generativeModel: GenerativeModel) : ViewModel() {

    private val _uiState: MutableStateFlow<SummarizeWaifuUiState> =
        MutableStateFlow(SummarizeWaifuUiState.Initial)
    val uiState: StateFlow<SummarizeWaifuUiState> =
        _uiState.asStateFlow()

    fun summarize(inputText: String) {
        _uiState.value = SummarizeWaifuUiState.Loading

        val prompt = "Summarize the following text for me: $inputText"

        viewModelScope.launch {
            // Non-streaming
            try {
                val response = generativeModel.generateContent(prompt)
                response.text?.let { outputContent ->
                    _uiState.value = SummarizeWaifuUiState.Success(outputContent)
                }
            } catch (e: Exception) {
                _uiState.value = SummarizeWaifuUiState.Error(e.localizedMessage ?: "")
            }
        }
    }

    fun summarizeStreaming(inputText: String) {
        _uiState.value = SummarizeWaifuUiState.Loading

        val prompt = "Summarize the following text for me: $inputText"

        viewModelScope.launch {
            try {
                var outputContent = ""
                generativeModel.generateContentStream(prompt)
                    .collect { response ->
                        outputContent += response.text
                        _uiState.value = SummarizeWaifuUiState.Success(outputContent)
                    }
            } catch (e: Exception) {
                _uiState.value = SummarizeWaifuUiState.Error(e.localizedMessage ?: "")
            }
        }
    }
}