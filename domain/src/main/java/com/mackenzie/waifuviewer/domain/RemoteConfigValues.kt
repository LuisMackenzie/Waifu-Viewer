package com.mackenzie.waifuviewer.domain

import javax.swing.UIManager.getString

data class RemoteConfigValues(
    val nsfwIsActive: Boolean = false,
    val gptIsActive: Boolean = false,
    val AutoModeIsEnabled: Boolean = false,
    val mode : Int = 0,
    var type: ServerType = ServerType.NORMAL
) {
    fun getTypes(type: String): ServerType {
        return when (type) {
            ServerType.NORMAL.value -> ServerType.NORMAL
            ServerType.ENHANCED.value -> ServerType.ENHANCED
            ServerType.NEKOS.value -> ServerType.NEKOS
            ServerType.FAVORITE.value -> ServerType.FAVORITE
            ServerType.WAIFUGPT.value -> ServerType.WAIFUGPT
            else -> ServerType.NORMAL
        }
    }
}
