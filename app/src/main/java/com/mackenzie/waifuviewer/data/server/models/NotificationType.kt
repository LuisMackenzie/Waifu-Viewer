package com.mackenzie.waifuviewer.data.server.models

import android.os.Parcelable
import com.mackenzie.waifuviewer.data.server.models.NotificationType.*
import kotlinx.parcelize.Parcelize

@Parcelize
enum class NotificationType(val code: Int) : Parcelable {
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