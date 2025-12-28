package com.mackenzie.waifuviewer.domain.video



data class StarsDetailedItems(
    val stars: List<DetailedStarDomainItem>,
    val count: Int
)


data class DetailedStarDomainItem(
    val star: DetailedStarInfoItem
)


data class DetailedStarInfoItem(
    val starName: String,
    val starThumb: String,
    val starUrl: String? = null,
    val videosCountAll: String? = null
)
