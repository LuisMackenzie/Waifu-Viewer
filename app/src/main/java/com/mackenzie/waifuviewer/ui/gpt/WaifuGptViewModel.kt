package com.mackenzie.waifuviewer.ui.gpt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mackenzie.waifuviewer.BuildConfig
import com.mackenzie.waifuviewer.domain.ImageRequestBody
import com.mackenzie.waifuviewer.domain.TextRequestBody
import com.mackenzie.waifuviewer.usecases.gen.GetImageResponseUseCase
import com.mackenzie.waifuviewer.usecases.gen.GetTextResponseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WaifuGptViewModel @Inject constructor(
    private val getTextResponseUseCase: GetTextResponseUseCase,
    private val getImageResponseUseCase: GetImageResponseUseCase
): ViewModel() {

    private val _state: MutableStateFlow<WaifuGptUiState> = MutableStateFlow(WaifuGptUiState.Initial)
    val state: StateFlow<WaifuGptUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {

        }
    }

    fun onGenerateText(prompt: String) {
        viewModelScope.launch {
            _state.value = WaifuGptUiState.Loading
            val response = getTextResponseUseCase(BuildConfig.openAiApikey, TextRequestBody(prompt))
        }
    }

    fun onGenerateImage(prompt: String) {
        viewModelScope.launch {
            _state.value = WaifuGptUiState.Loading
            val response = getImageResponseUseCase(BuildConfig.openAiApikey, ImageRequestBody(prompt))
        }
    }


}