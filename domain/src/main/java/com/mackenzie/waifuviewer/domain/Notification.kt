package com.mackenzie.waifuviewer.domain

import java.util.Date

data class Notification(
    val id: Int,
    val pushId: String,
    val date: Date?,
    val title: String,
    val description: String,
    val imageUrl: String,
    var isRead: Boolean,
    var type: NotificationType,
)

fun List<Notification>.orderByDate() = this.sortedByDescending { it.date }
