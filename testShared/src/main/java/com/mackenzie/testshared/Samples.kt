package com.mackenzie.testshared

import com.mackenzie.waifuviewer.domain.FavoriteItem
import com.mackenzie.waifuviewer.domain.WaifuImItem
import com.mackenzie.waifuviewer.domain.WaifuPicItem

val sampleImWaifu = WaifuImItem(
    id = 0,
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