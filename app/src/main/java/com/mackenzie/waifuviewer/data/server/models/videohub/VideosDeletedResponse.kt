package com.mackenzie.waifuviewer.data.server.models.videohub

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VideosDeletedResponse(
    @Json(name = "deleted") val deleted: DeletedInfo
)

@JsonClass(generateAdapter = true)
data class DeletedInfo(
    @Json(name = "count") val count: Int,
    @Json(name = "videos") val videos: List<DeletedVideo>
)

@JsonClass(generateAdapter = true)
data class DeletedVideo(
    @Json(name = "video_id") val videoId: String,
    @Json(name = "deleted") val deleted: String
)

