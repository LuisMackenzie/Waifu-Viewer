package com.mackenzie.waifuviewer.domain.im

data class TagItem(
    val description: String,
    val isNsfw: Boolean,
    val name: String,
    val tagId: Int
)
