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

fun getType(id: Int): VideoItem.Type {
    return when (id % 5) {
        0 -> VideoItem.Type.VIDEO
        1 -> VideoItem.Type.AUDIO
        else -> VideoItem.Type.PHOTO
    }
}