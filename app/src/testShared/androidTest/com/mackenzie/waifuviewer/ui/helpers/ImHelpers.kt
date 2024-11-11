package com.mackenzie.waifuviewer.ui

import com.mackenzie.waifuviewer.data.WaifusImRepository
import com.mackenzie.waifuviewer.data.db.*
import com.mackenzie.waifuviewer.data.db.datasources.FavoriteDataSource
import com.mackenzie.waifuviewer.data.db.datasources.RoomImDataSource
import com.mackenzie.waifuviewer.data.server.*
import com.mackenzie.waifuviewer.data.server.models.RemoteConnect
import com.mackenzie.waifuviewer.data.server.models.WaifuIm
import com.mackenzie.waifuviewer.ui.fakes.*

fun buildImRepositoryWith(
    localData: List<WaifuImDbItem>,
    remoteData: List<WaifuIm>
): WaifusImRepository {
    val localImDataSource = RoomImDataSource(FakeWaifuImDao(localData))
    val favoriteDataSource = FavoriteDataSource(FakeFavoriteDao())
    val remoteImDataSource = ServerImDataSource( RemoteConnect(
        FakeRemoteImService(remoteData),
        FakeRemotePicsService(listOf()),
        FakeRemoteBestService(listOf()),
        FakeRemoteMoeService(listOf())
    )
    )
    return WaifusImRepository(localImDataSource, favoriteDataSource , remoteImDataSource)
}

fun buildImDatabaseWaifus(vararg id: Int) = id.map {
    WaifuImDbItem(
        id = it,
        signature = "",
        extension = "",
        dominantColor = "",
        source = "Overview $it",
        uploadedAt = "",
        isNsfw = false,
        width = "",
        height = "",
        imageId = 6969,
        url = "https://cdn.waifu.im/5f7e656343cb7be1.jpg",
        previewUrl = "https://cdn.waifu.im/5f7e656343cb7be1.jpg",
        isFavorite = false
    )
}

fun buildImRemoteWaifus(vararg id: Int) = id.map {
    WaifuIm(
        signature = "",
        extension = "",
        imageId = 6969,
        favourites = 0,
        dominant_color = "",
        source = "Overview $it",
        uploadedAt = "",
        likedAt = null,
        isNsfw = false,
        height = "",
        width = "",
        url = "https://cdn.waifu.im/5f7e656343cb7be1.jpg",
        previewUrl = ""
        // tags = ,

    )
}