package com.mackenzie.waifuviewer.data.server.models.videohub

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VideoSearchResponse(
    @Json(name = "videos") val videos: List<Video>,
    @Json(name = "count") val count: Int
)

@JsonClass(generateAdapter = true)
data class Video(
    @Json(name = "video") val video: VideoDetails
)

@JsonClass(generateAdapter = true)
data class VideoDetails(
    @Json(name = "video_id") val videoId: String,
    @Json(name = "title") val title: String,
    @Json(name = "thumb") val thumb: String,
    @Json(name = "url") val url: String,
    @Json(name = "publish_date") val publishDate: String,
    @Json(name = "rating") val rating: String,
    @Json(name = "ratings") val ratings: String,
    @Json(name = "views") val views: String,
    @Json(name = "duration") val duration: String,
    @Json(name = "default_thumb") val defaultThumb: String,
    @Json(name = "thumbs") val thumbs: List<Thumb>? = null,
    @Json(name = "tags") val tags: List<Tag>? = null,
    @Json(name = "stars") val stars: List<Star>? = null
)

@JsonClass(generateAdapter = true)
data class Thumb(
    @Json(name = "size") val size: String,
    @Json(name = "width") val width: String,
    @Json(name = "height") val height: String,
    @Json(name = "src") val src: String
)

@JsonClass(generateAdapter = true)
data class Tag(
    @Json(name = "tag_name") val tagName: String
)

@JsonClass(generateAdapter = true)
data class Star(
    @Json(name = "star") val star: StarInfo
)

@JsonClass(generateAdapter = true)
data class StarInfo(
    @Json(name = "star_name") val starName: String,
    @Json(name = "star_thumb") val starThumb: String? = null
)

