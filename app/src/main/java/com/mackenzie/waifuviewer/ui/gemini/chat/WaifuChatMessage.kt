package com.mackenzie.waifuviewer.ui.gemini.chat

import java.util.UUID

data class WaifuChatMessage(
    val id: String = UUID.randomUUID().toString(),
    var text: String = "",
    val participant: Participant = Participant.USER,
    var isPending: Boolean = false
)

enum class Participant {
    USER, MODEL, ERROR
}