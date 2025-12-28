package com.mackenzie.waifuviewer.domain.video


data class VideoByIdRItem(
    val video: VideoDetailsById
)


data class VideoDetailsById(
    val videoId: String,
    val title: String,
    val thumb: String,
    val url: String,
    val embedUrl: String,
    val publishDate: String,
    val rating: String,
    val ratings: String,
    val views: String,
    val duration: String,
    val defaultThumb: String,
    val thumbs: List<ThumbItem>? = null,
    val tags: List<TagItem>? = null,
    val stars: List<StarItem>? = null
)
