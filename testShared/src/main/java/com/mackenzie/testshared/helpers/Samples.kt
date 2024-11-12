package com.mackenzie.testshared

import com.mackenzie.waifuviewer.domain.FavoriteItem
import com.mackenzie.waifuviewer.domain.WaifuBestItem
import com.mackenzie.waifuviewer.domain.im.WaifuImItem
import com.mackenzie.waifuviewer.domain.WaifuPicItem
import com.mackenzie.waifuviewer.domain.im.ArtistIm

val sampleImWaifu = WaifuImItem(
    id = 0,
    artist = ArtistIm("","","","","",""),
    byteSize = 0,
    signature = "",
    extension = ".jpg",
    dominantColor = "",
    source = "",
    uploadedAt = "",
    isNsfw = false,
    width = "",
    height = "",
    imageId = 6969,
    url = "https://cdn.waifu.im/5f7e656343cb7be1.jpg",
    previewUrl = "https://cdn.waifu.im/5f7e656343cb7be1.jpg" ,
    tags = emptyList(),
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