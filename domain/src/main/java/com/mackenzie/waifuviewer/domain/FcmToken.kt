package com.mackenzie.waifuviewer.domain

data class FcmToken(
    val id: Int,
    val token: String,
    val validUtil: String,
    val deviceBrand: String,
    val deviceModel: String
)
