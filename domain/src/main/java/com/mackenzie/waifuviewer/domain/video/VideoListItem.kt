package com.mackenzie.waifuviewer.domain.video


data class VideoListItem(
    val videos: List<VideoItem>,
    val count: Int
)


data class VideoItem(
    val video: VideoItemDetails
)

data class VideoItemDetails(
    val videoId: String,
    val title: String,
    val thumb: String,
    val url: String,
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

data class ThumbItem(
    val size: String,
    val width: String,
    val height: String,
    val src: String
)


data class TagItem(
    val tagName: String
)

data class StarItem(
    val star: StarInfoItem
)

data class StarInfoItem(
    val starName: String,
    val starThumb: String? = null
)
