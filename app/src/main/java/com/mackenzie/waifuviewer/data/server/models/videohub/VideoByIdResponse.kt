package com.mackenzie.waifuviewer.data.server.models.videohub

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VideoByIdResponse(
    @Json(name = "video") val video: VideoDetailsById
)

@JsonClass(generateAdapter = true)
data class VideoDetailsById(
    @Json(name = "video_id") val videoId: String,
    @Json(name = "title") val title: String,
    @Json(name = "thumb") val thumb: String,
    @Json(name = "url") val url: String,
    @Json(name = "embed_url") val embedUrl: String,
    @Json(name = "publish_date") val publishDate: String,
    @Json(name = "rating") val rating: String,
    @Json(name = "ratings") val ratings: String,
    @Json(name = "views") val views: String,
    @Json(name = "duration") val duration: String,
    @Json(name = "default_thumb") val defaultThumb: String,
    @Json(name = "thumbs") val thumbs: List<ThumbResponse>? = null,
    @Json(name = "tags") val tags: List<TagResponse>? = null,
    @Json(name = "stars") val stars: List<StarResponse>? = null
)

