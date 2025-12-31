package com.mackenzie.waifuviewer.domain

data class VideoItem(
    val id: Int,
    val title: String,
    val thumb: String,
    val url: String,
    val type: Type,
    val description: String
) {
    enum class Type { PHOTO, VIDEO, AUDIO }
}

fun getMedia() = (1..20).map {
    VideoItem(
        it,
        "Title $it",
        "https://loremflickr.com/400/400/cat?lock=1",
        "https://loremflickr.com/400/400/girl?lock=$it",
        getType(it),
        "Generic Description $it"
    )
}

fun getMedia2() = (21..30).map {
    VideoItem(
        it,
        "Title $it",
        "https://loremflickr.com/400/400/cat?lock=1",
        "https://loremflickr.com/400/400/girl?lock=$it",
        getType(it),
        "Generic Description $it"
    )
}

fun getVideoServers() = (1..10).map {
    VideoItem(
        it,
        getNameById(it),
        "https://loremflickr.com/400/400/cat?lock=1",
        getImageUrlById(it),
        getType(it),
        "Generic Description of ${getNameById(it)}"
    )
}

fun getType(id: Int): VideoItem.Type {
    return when (id % 5) {
        0 -> VideoItem.Type.VIDEO
        1 -> VideoItem.Type.AUDIO
        else -> VideoItem.Type.PHOTO
    }
}

fun getNameById(id: Int): String {
    return when (id) {
        1 -> "PornHub.com"
        2 -> "RedTube"
        3 -> "Server 03"
        4 -> "Server 04"
        5 -> "Server 05"
        6 -> "Server 06"
        7 -> "Server 07"
        8 -> "Server 08"
        9 -> "Server 09"
        10 -> "Server 10"
        else -> "Server Name Unknown"
    }
}

fun getImageUrlById(id: Int): String {
    return when (id) {
        // 1 -> "PornHub.com"
        2 -> "https://avatars.githubusercontent.com/u/46095600?s=200&v=4"
        /*3 -> "Server 03"
        4 -> "Server 04"
        5 -> "Server 05"
        6 -> "Server 06"
        7 -> "Server 07"
        8 -> "Server 08"
        9 -> "Server 09"
        10 -> "Server 10"*/
        else -> "https://loremflickr.com/400/400/girl?lock=$id"
    }
}