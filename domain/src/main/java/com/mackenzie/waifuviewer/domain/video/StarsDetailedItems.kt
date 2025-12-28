package com.mackenzie.waifuviewer.domain.video



data class StarsDetailedItems(
    val stars: List<DetailedStarItem>,
    val count: Int
)


data class DetailedStarItem(
    val star: DetailedStarInfo
)


data class DetailedStarInfo(
    val starName: String,
    val starThumb: String,
    val starUrl: String? = null,
    val videosCountAll: String? = null
)
