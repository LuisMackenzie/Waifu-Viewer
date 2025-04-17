package com.mackenzie.waifuviewer.data.server.push

import android.app.PendingIntent
import android.content.res.Resources
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.domain.Notification
import com.mackenzie.waifuviewer.domain.NotificationType
import com.mackenzie.waifuviewer.ui.common.urlToBitmap
import java.util.Date
import java.util.UUID


fun NotificationCompat.Builder.setUpBuilder(
    icon: Int,
    title: String,
    description: String,
    imageUrl: String,
    intent: PendingIntent? = null
): NotificationCompat.Builder {

    return this.setSmallIcon(icon)
        .setContentTitle(title)
        .setContentText(description)
        .setAutoCancel(true)
        .setOnlyAlertOnce(true)
        .setVibrate(longArrayOf(1000, 1000, 1000, 1000))
        .setContentIntent(intent)
        .setStyle(
            NotificationCompat.BigPictureStyle()
                .bigPicture(imageUrl.urlToBitmap())
        )

}

fun NotificationCompat.Builder.setUpBuilderAndBigPicture(
    icon: Int,
    title: String,
    res: Resources,
    description: String,
    imageUrl: String,
    intent: PendingIntent? = null
): NotificationCompat.Builder {

    return this.setSmallIcon(icon)
        .setContentTitle(title)
        .setContentText(description)
        .setAutoCancel(true)
        .setOnlyAlertOnce(true)
        .setColor(res.getColor(R.color.purple_200, null))
        .setVibrate(longArrayOf(1000, 1000, 1000, 1000))
        .setContentIntent(intent)
        .setStyle(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                NotificationCompat.BigPictureStyle()
                    .bigPicture(imageUrl.urlToBitmap())
                    .showBigPictureWhenCollapsed(true)
            } else {
                NotificationCompat.BigPictureStyle()
                    .bigPicture(imageUrl.urlToBitmap())
            }
        )
}

fun createCustomNotification(
    type: NotificationType,
    title: String,
    body: String,
    imageUrl: String,
    id: String? = null
) =
    Notification(
        id = 0,
        pushId = id ?: UUID.randomUUID().mostSignificantBits.toString(),
        date = Date(),
        title = title,
        description = body,
        imageUrl = imageUrl,
        isRead = false,
        type = type
    )