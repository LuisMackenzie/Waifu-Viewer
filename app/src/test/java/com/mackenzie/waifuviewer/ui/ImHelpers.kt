package com.mackenzie.waifuviewer.ui

import com.mackenzie.waifuviewer.data.WaifusImRepository
import com.mackenzie.waifuviewer.data.db.*
import com.mackenzie.waifuviewer.data.server.*
import com.mackenzie.waifuviewer.ui.fakes.*

fun buildImRepositoryWith(
    localData: List<WaifuImDbItem>,
    remoteData: List<WaifuIm>
): WaifusImRepository {
    val localImDataSource = RoomImDataSource(FakeWaifuImDao(localData))
    val favoriteDataSource = FavoriteDataSource(FakeFavoriteDao())
    val remoteImDataSource = ServerImDataSource( FakeRemoteImService(remoteData))
    return WaifusImRepository(localImDataSource, favoriteDataSource , remoteImDataSource)
}

fun buildDatabaseImMovies(vararg id: Int) = id.map {
    WaifuImDbItem(
        id = it,
        dominantColor = "",
        file = "Overview $it",
        height = "",
        imageId = 6969,
        isNsfw = false,
        url = "https://cdn.waifu.im/5f7e656343cb7be1.jpg",
        width = "",
        isFavorite = false
    )
}

fun buildRemoteImMovies(vararg id: Int) = id.map {
    WaifuIm(
        dominant_color = "",
        extension = "",
        favourites = 0,
        file = "Overview $it",
        height = "",
        imageId = 6969,
        isNsfw = false,
        previewUrl = "",
        source = "",
        // tags = ,
        uploadedAt = "",
        url = "https://cdn.waifu.im/5f7e656343cb7be1.jpg",
        width = ""
    )
}