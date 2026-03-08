package com.mackenzie.waifuviewer.data.server.models.videohub

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StarsResponse(
    @Json(name = "stars") val stars: List<StarItem>,
    @Json(name = "count") val count: Int
)

@JsonClass(generateAdapter = true)
data class StarItem(
    @Json(name = "star") val star: StarBasicInfo
)

@JsonClass(generateAdapter = true)
data class StarBasicInfo(
    @Json(name = "star_name") val starName: String,
    @Json(name = "star_thumb") val starThumb: String
)

