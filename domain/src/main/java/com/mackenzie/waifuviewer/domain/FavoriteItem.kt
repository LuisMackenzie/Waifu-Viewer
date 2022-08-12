package com.mackenzie.waifuviewer.domain

data class FavoriteItem(
    val id: Int,
    val url: String,
    val title: String,
    val isFavorite: Boolean
    )
