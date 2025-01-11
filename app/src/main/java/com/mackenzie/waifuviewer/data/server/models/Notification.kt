package com.mackenzie.waifuviewer.data.server.models

import java.util.Date

data class Notification(
    val id: String,
    val date: Date?,
    val title: String,
    val description: String,
    var isRead: Boolean,
    var type: NotificationType,
)

fun List<Notification>.orderByDate() = this.sortedByDescending { it.date }