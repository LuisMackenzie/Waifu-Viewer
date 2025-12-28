package com.mackenzie.waifuviewer.domain.video


data class StarsItems(
    val stars: List<StarItem>,
    val count: Int
)


data class StarItem(
    val star: StarBasicInfo
)


data class StarBasicInfo(
    val starName: String,
    val starThumb: String
)
