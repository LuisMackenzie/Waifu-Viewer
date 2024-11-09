package com.mackenzie.waifuviewer.ui.gemini.chat

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.toMutableStateList

class WaifuChatUiState(messages: List<WaifuChatMessage> = emptyList()) {


    private val _messages: MutableList<WaifuChatMessage> = messages.toMutableStateList()
    val messages: List<WaifuChatMessage> = _messages

    fun addMessage(msg: WaifuChatMessage) {
        _messages.add(msg)
    }

    fun replaceLastPendingMessage() {
        val lastMessage = _messages.lastOrNull()
        lastMessage?.let {
            val newMessage = lastMessage.apply { isPending = false }
            _messages.removeAt(_messages.lastIndex)
            _messages.add(newMessage)
        }
    }

}