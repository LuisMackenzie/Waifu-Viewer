package com.mackenzie.waifuviewer.domain


data class WaifuImItem (
    val id: Int,
    val dominant_color: String,
    val file: String,
    val height: String,
    val imageId: Int,
    val isNsfw: Boolean,
    val url: String,
    val width: String,
    val isFavorite: Boolean
    )