package com.mackenzie.waifuviewer.domain

data class RemoteConfigValues(
    val nsfwIsActive: Boolean = false,
    val gptIsActive: Boolean = false,
    val AutoModeIsEnabled: Boolean = false,
    val mode : Int = 0,
    val serverType: ServerType? = null
)
