package com.mackenzie.waifuviewer.domain

data class WaifuBestItemPng(
    val id: Int,
    val artistHref: String,
    val artistName: String,
    val sourceUrl: String,
    val url: String,
    val isFavorite: Boolean
)

data class WaifuBestItemGif(
    val id: Int,
    val animeName: String,
    val url: String,
    val isFavorite: Boolean
)
