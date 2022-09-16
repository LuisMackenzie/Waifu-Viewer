package com.mackenzie.waifuviewer.domain


data class WaifuImItem (
    val id: Int,
    val signature: String,
    val extension: String,
    val dominantColor: String,
    val source: String,
    val uploadedAt: String,
    val isNsfw: Boolean,
    val width: String,
    val height: String,
    val imageId: Int,
    val url: String,
    val previewUrl: String,
    // val tags: List<Tag>,
    val isFavorite: Boolean
    )
