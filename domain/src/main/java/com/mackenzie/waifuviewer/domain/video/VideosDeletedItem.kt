package com.mackenzie.waifuviewer.domain.video

data class VideosDeletedItem(
    val deleted: DeletedInfo
)

data class DeletedInfo(
    val count: Int,
    val videos: List<DeletedVideo>
)

data class DeletedVideo(
    val videoId: String,
    val deleted: String
)
