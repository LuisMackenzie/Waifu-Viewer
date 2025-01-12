package com.mackenzie.waifuviewer.data.server.push

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.FcmToken
import com.mackenzie.waifuviewer.domain.Notification
import com.mackenzie.waifuviewer.usecases.push.SaveNotificationUseCase
import com.mackenzie.waifuviewer.usecases.push.SaveTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FcmViewModel @Inject constructor(
    private val saveTokenUseCase: SaveTokenUseCase,
    private val saveNotificationUseCase: SaveNotificationUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    private fun saveToken(token: FcmToken) {
        viewModelScope.launch {
            saveTokenUseCase(token)?.let {
                _state.value = UiState(error = it)
            }
        }
    }

    private fun saveNotification(notification: Notification) {
        viewModelScope.launch {
            saveNotificationUseCase(notification)?.let {
                _state.value = UiState(error = it)
            }
        }
    }

    fun onTokenReceived(token: FcmToken) {
        _state.value = UiState(token = token)
        saveToken(token)
    }

    fun onNotificationReceived(notification: Notification) {
        _state.value = UiState(notification = notification)
        saveNotification(notification)
    }

    fun onTestReceived(token: FcmToken, notification: Notification?) {
        _state.value = UiState(token = token, notification = notification)
        Log.e("FcmViewModel", "onTestReceived: $token, $notification")
        Log.e("FcmViewModel", "state: token${state.value.token}, push${state.value.notification}")
        // saveToken(token)
        // saveNotification(notification)
    }


    data class UiState(
        val token: FcmToken? = null,
        val notification: Notification? = null,
        val error: Error? = null
    )
}