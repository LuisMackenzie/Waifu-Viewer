package com.mackenzie.waifuviewer.domain.im


data class WaifuImItem (
    val id: Int,
    val artist: ArtistIm,
    val byteSize: Long,
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
    val tags: List<TagItem?>,
    val isFavorite: Boolean
    )
