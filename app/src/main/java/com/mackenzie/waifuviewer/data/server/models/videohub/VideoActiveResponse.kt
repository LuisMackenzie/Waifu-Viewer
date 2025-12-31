package com.mackenzie.waifuviewer.data.server.models.videohub

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VideoActiveResponse(
    @Json(name = "active") val active: ActiveInfoResponse
)

@JsonClass(generateAdapter = true)
data class ActiveInfoResponse(
    @Json(name = "active") val active: String
)

