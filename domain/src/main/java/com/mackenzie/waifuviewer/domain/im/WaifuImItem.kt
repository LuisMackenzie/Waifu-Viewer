package com.mackenzie.waifuviewer.domain.im


data class WaifuImItem (
    val id: Int,
    val imageId: Int,
    val perceptualHash: String,
    val extension: String,
    val dominantColor: String,
    val source: String?,
    val artist: List<ArtistIm?>,
    val uploadedId: String?,
    val uploadedAt: String?,
    val isNsfw: Boolean?,
    val isAnimated: Boolean?,
    val width: String?,
    val height: String?,
    val byteSize: Long?,
    val url: String,
    val tags: List<TagItem?>,
    val favorites: Int?,
    val isFavorite: Boolean
    )
