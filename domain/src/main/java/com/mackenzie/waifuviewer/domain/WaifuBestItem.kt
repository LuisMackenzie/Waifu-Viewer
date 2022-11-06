package com.mackenzie.waifuviewer.domain

data class WaifuBestItem(
    val id: Int,
    val artistHref: String,
    val artistName: String,
    val animeName: String,
    val sourceUrl: String,
    val url: String,
    val isFavorite: Boolean
)
