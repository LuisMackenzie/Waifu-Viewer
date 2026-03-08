package com.mackenzie.waifuviewer.domain.im

data class WaifuImTagList(
    val id: Int,
    val versatile: List<TagItem>,
    val nsfw: List<TagItem>
)
