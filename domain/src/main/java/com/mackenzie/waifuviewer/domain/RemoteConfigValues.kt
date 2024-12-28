package com.mackenzie.waifuviewer.domain

data class RemoteConfigValues(
    val nsfwIsActive: Boolean = false,
    val gptIsActive: Boolean = false,
    val geminiIsActive: Boolean = false,
    val AutoModeIsEnabled: Boolean = false,
    var isFavorite: Boolean = false,
    var mode : Int = 0,
    var type: ServerType? = null
)
