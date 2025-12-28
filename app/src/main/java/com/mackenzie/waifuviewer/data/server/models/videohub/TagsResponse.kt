package com.mackenzie.waifuviewer.data.server.models.videohub

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TagsResponse(
    @Json(name = "tags") val tags: List<TagItem>,
    @Json(name = "count") val count: Int
)

@JsonClass(generateAdapter = true)
data class TagItem(
    @Json(name = "tag") val tag: TagInfo
)

@JsonClass(generateAdapter = true)
data class TagInfo(
    @Json(name = "tag_name") val tagName: String
)

