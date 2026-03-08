package com.mackenzie.waifuviewer.data.server.models.videohub

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CategoriesResponse(
    @Json(name = "categories") val categories: List<CategoryResponse>,
    @Json(name = "count") val count: Int
)

@JsonClass(generateAdapter = true)
data class CategoryResponse(
    @Json(name = "category") val category: String
)

