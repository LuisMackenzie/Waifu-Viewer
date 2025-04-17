package com.mackenzie.waifuviewer.data.server.push

import android.app.PendingIntent
import android.content.res.Resources
import androidx.core.app.NotificationCompat
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.domain.Notification
import com.mackenzie.waifuviewer.domain.NotificationType
import java.util.Date
import java.util.UUID

fun NotificationCompat.Builder.setUpBuilder(
    icon: Int,
    title: String,
    description: String,
    intent: PendingIntent? = null
): NotificationCompat.Builder {

    return this.setSmallIcon(icon)
        .setAutoCancel(true)
        .setContentTitle(title)
        .setContentText(description)
        .setVibrate(longArrayOf(1000, 1000, 1000, 1000))
        .setOnlyAlertOnce(true)
        .setContentIntent(intent)
}

fun NotificationCompat.Builder.setUpBuilderWithBackground(
    icon: Int,
    title: String,
    res: Resources,
    description: String,
    intent: PendingIntent? = null
): NotificationCompat.Builder {

    return this.setSmallIcon(icon)
        .setAutoCancel(true)
        .setContentTitle(title)
        .setContentText(description)
        .setColor(res.getColor(R.color.purple_100, null))
        .setVibrate(longArrayOf(1000, 1000, 1000, 1000))
        .setOnlyAlertOnce(true)
        .setContentIntent(intent)
}

fun createCustomNotification(
    type: NotificationType,
    title: String,
    body: String,
    id: String? = null
) =
    Notification(
        id = 0,
        pushId = id ?: UUID.randomUUID().mostSignificantBits.toString(),
        date = Date(),
        title = title,
        description = body,
        isRead = false,
        type = type
    )