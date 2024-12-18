package com.mackenzie.waifuviewer.domain.selector

data class TagsState(
    val normal: ImTags = ImTags(),
    val enhanced: PicsTags = PicsTags(),
    val nekos: NekosTags = NekosTags()
)

data class ImTags(
    val sfw: List<String> = ServerTags().normalSfw,
    val nsfw: List<String> = ServerTags().normalNsfw
)

data class PicsTags(
    val sfw: List<String>  = ServerTags().enhancedSfw,
    val nsfw: List<String>  = ServerTags().enhancedNsfw
)

data class NekosTags(
    val png: List<String>  = ServerTags().nekosPng,
    val gifs: List<String>  = ServerTags().nekosGif,
    val all: List<String>  = ServerTags().nekosAll
)

