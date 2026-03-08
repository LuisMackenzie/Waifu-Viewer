package com.mackenzie.testshared

import com.mackenzie.waifuviewer.domain.FavoriteItem
import com.mackenzie.waifuviewer.domain.WaifuBestItem
import com.mackenzie.waifuviewer.domain.im.WaifuImItem
import com.mackenzie.waifuviewer.domain.WaifuPicItem
import com.mackenzie.waifuviewer.domain.im.ArtistIm

val sampleImWaifu = WaifuImItem(
    id = 0,
    imageId = 6969,
    perceptualHash = "a1b2c3d4e5f6g7h8i9j0",
    extension = ".jpg",
    dominantColor = "",
    source = "",
    artist = ArtistIm("","","","","",""),
    uploadedId = "",
    uploadedAt = "",
    isNsfw = false,
    isAnimated = false,
    width = "",
    height = "",
    byteSize = 0,
    url = "https://cdn.waifu.im/5f7e656343cb7be1.jpg",
    tags = emptyList(),
    favorites = 23,
    isFavorite = false
)

val samplePicWaifu = WaifuPicItem(
    id = 0,
    url = "https://cdn.waifu.im/5f7e656343cb7be1.jpg",
    isFavorite = false
)

val sampleFavWaifu = FavoriteItem(
    id = 0,
    title = "Waifu Test",
    url = "https://cdn.waifu.im/5f7e656343cb7be1.jpg",
    isFavorite = false
)

val sampleBestWaifu = WaifuBestItem(
    id = 0,
    artistHref = "",
    artistName = "Artist Name",
    animeName = "Anime Name",
    sourceUrl = "https://cdn.waifu.im/5f7e656343cb7be1.jpg",
    url = "https://cdn.waifu.im/5f7e656343cb7be1.jpg",
    isFavorite = false
)