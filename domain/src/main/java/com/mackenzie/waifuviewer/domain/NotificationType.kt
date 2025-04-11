package com.mackenzie.waifuviewer.domain

import com.mackenzie.waifuviewer.domain.NotificationType.*

enum class NotificationType(val code: Int) {
    NEWS(0),
    UPDATES(1),
    CALL_TO_ACTION(2),
    CALL_TO_RESPONSE(3),
    EXPIRED(-1)
}

fun Int.toNotificationType(): NotificationType = when (this) {
    0 -> NEWS
    1 -> UPDATES
    2 -> CALL_TO_ACTION
    3 -> CALL_TO_RESPONSE
    else -> EXPIRED
}