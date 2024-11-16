package com.mackenzie.waifuviewer.domain

data class SwitchState(
    var nsfw: Boolean = false,
    var gif: Boolean = false,
    var portrait: Boolean = false
)
