package com.mackenzie.waifuviewer.domain

data class AnimeSearchItem(
    val id : Int,
    val anilist: Int,
    val filename: String,
    val episode: Int,
    val from: Float,
    val to: Float,
    val similarity: Float,
    val video: String,
    val image: String
)
