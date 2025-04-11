package com.mackenzie.waifuviewer.domain

data class FcmToken(
    val id: Int,
    val token: String,
    val createdAt: String,
    val validUntil: String,
    val deviceBrand: String,
    val deviceModel: String,
    val deviceId: String
)
