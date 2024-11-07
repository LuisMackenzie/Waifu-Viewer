package com.mackenzie.waifuviewer.domain

import javax.swing.UIManager.getString

data class RemoteConfigValues(
    val nsfwIsActive: Boolean = false,
    val gptIsActive: Boolean = false,
    val geminiIsActive: Boolean = false,
    val AutoModeIsEnabled: Boolean = false,
    val mode : Int = 0,
    var type: ServerType = ServerType.NORMAL
)
