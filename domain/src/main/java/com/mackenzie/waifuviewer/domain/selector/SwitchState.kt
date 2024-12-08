package com.mackenzie.waifuviewer.domain.selector

data class SwitchState(
    val nsfw: Boolean = false,
    val gifs: Boolean = false,
    val portrait: Boolean = false
)