package com.mackenzie.waifuviewer.ui.gpt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mackenzie.waifuviewer.data.toError
import com.mackenzie.waifuviewer.ui.common.errorToString
import com.mackenzie.waifuviewer.usecases.video.GetVideoServersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoHubViewModel @Inject constructor(
    private val getVideoServersUseCase: GetVideoServersUseCase
): ViewModel() {

    private val _state = MutableStateFlow(VideoHubUiState())
    val state: StateFlow<VideoHubUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            // getVideoServersUseCase(" apiKey" )
                // .catch { error -> _state.update { it.copy(error = cause.toError()) }}
                // .collect{ videoServerResponse -> _state.update { VideoHubUiState( videoServerResponse = videoServerResponse) } }
        }
    }

    fun getServers() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true,  error = null) }
            val error = getVideoServersUseCase("apiKey")
            if (error != null) {
                _state.update { it.copy(isLoading = false, error = error.toString()) }
            } else {
                _state.update { it.copy(isLoading = false, error = null, videoServerResponse = "Video servers fetched successfully") }
            }
        }
    }


    data class VideoHubUiState(
        val isLoading: Boolean = false,
        val error: String? = null,
        val videoServerResponse: String? = null
    )


}