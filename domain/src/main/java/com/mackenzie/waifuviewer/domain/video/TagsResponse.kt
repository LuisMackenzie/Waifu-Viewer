package com.mackenzie.waifuviewer.domain.video


data class TagsResponseItem(
    val tags: List<TagDomainItem>,
    val count: Int
)


data class TagDomainItem(
   val tag: TagDomainInfo
)

data class TagDomainInfo(
    val tagName: String
)
