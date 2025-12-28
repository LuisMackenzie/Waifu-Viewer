package com.mackenzie.waifuviewer.domain.video


data class TagsResponse(
    val tags: List<TagItem>,
    val count: Int
)


data class TagItem(
   val tag: TagInfo
)

data class TagInfo(
    val tagName: String
)
