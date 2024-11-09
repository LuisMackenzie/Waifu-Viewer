package com.mackenzie.waifuviewer.domain

enum class ServerType(val value: String)  {
    NORMAL("normal"),
    ENHANCED("enhanced"),
    NEKOS("nekos"),
    FAVORITE("favorite"),
    WAIFUGPT("waifugpt"),
    WAIFUGEMINI("waifugemini")
}

fun String.getTypes(): ServerType {
    return when (this) {
        ServerType.NORMAL.value -> ServerType.NORMAL
        ServerType.ENHANCED.value -> ServerType.ENHANCED
        ServerType.NEKOS.value -> ServerType.NEKOS
        ServerType.FAVORITE.value -> ServerType.FAVORITE
        ServerType.WAIFUGPT.value -> ServerType.WAIFUGPT
        ServerType.WAIFUGEMINI.value -> ServerType.WAIFUGEMINI
        else -> ServerType.NORMAL
    }
}
