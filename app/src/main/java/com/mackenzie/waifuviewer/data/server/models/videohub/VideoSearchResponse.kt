package com.mackenzie.waifuviewer.data.server.models.videohub

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VideoSearchResponse(
    @Json(name = "videos") val videos: List<VideoResponse>,
    @Json(name = "count") val count: Int
)

@JsonClass(generateAdapter = true)
data class VideoResponse(
    @Json(name = "video") val video: VideoDetails
)

@JsonClass(generateAdapter = true)
data class VideoDetails(
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

@JsonClass(generateAdapter = true)
data class ThumbResponse(
    @Json(name = "size") val size: String,
    @Json(name = "width") val width: String,
    @Json(name = "height") val height: String,
    @Json(name = "src") val src: String
)

@JsonClass(generateAdapter = true)
data class TagResponse(
    @Json(name = "tag_name") val tagName: String
)

@JsonClass(generateAdapter = true)
data class StarResponse(
    @Json(name = "star") val star: StarInfoResponse
)

@JsonClass(generateAdapter = true)
data class StarInfoResponse(
    @Json(name = "star_name") val starName: String,
    @Json(name = "star_thumb") val starThumb: String? = null
)

