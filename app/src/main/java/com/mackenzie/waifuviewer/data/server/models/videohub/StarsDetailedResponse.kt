package com.mackenzie.waifuviewer.data.server.models.videohub

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StarsDetailedResponse(
    @Json(name = "stars") val stars: List<DetailedStarItem>,
    @Json(name = "count") val count: Int
)

@JsonClass(generateAdapter = true)
data class DetailedStarItem(
    @Json(name = "star") val star: DetailedStarInfo
)

@JsonClass(generateAdapter = true)
data class DetailedStarInfo(
    @Json(name = "star_name") val starName: String,
    @Json(name = "star_thumb") val starThumb: String,
    @Json(name = "star_url") val starUrl: String? = null,
    @Json(name = "videos_count_all") val videosCountAll: String? = null
)

