package com.mackenzie.waifuviewer.domain.video


data class StarsItems(
    val stars: List<StarDomainItem>,
    val count: Int
)


data class StarDomainItem(
    val star: StarBasicDomainInfo
)


data class StarBasicDomainInfo(
    val starName: String,
    val starThumb: String
)
