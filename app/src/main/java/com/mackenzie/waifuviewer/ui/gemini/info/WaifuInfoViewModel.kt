package com.mackenzie.waifuviewer.ui.gemini.info

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WaifuInfoViewModel(private val generativeModel: GenerativeModel) : ViewModel() {

    private val _uiState: MutableStateFlow<WaifuInfoUiState> =
        MutableStateFlow(WaifuInfoUiState.Initial)
    val uiState: StateFlow<WaifuInfoUiState> =
        _uiState.asStateFlow()

    fun reason(
        userInput: String,
        selectedImages: List<Bitmap>
    ) {
        _uiState.value = WaifuInfoUiState.Loading
        val prompt = "Fijate en la imagen, y entonces responde la siguiente question: $userInput"

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val inputContent = content {
                    for (bitmap in selectedImages) {
                        image(bitmap)
                    }
                    text(prompt)
                }

                var outputContent = ""

                generativeModel.generateContentStream(inputContent)
                    .collect { response ->
                        outputContent += response.text
                        _uiState.value = WaifuInfoUiState.Success(outputContent)
                    }
            } catch (e: Exception) {
                _uiState.value = WaifuInfoUiState.Error(e.localizedMessage ?: "")
            }
        }
    }

}