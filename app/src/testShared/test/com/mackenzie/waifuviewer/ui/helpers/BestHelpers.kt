package com.mackenzie.waifuviewer.ui.helpers

import com.mackenzie.waifuviewer.data.WaifusBestRepository
import com.mackenzie.waifuviewer.data.db.WaifuBestDbItem
import com.mackenzie.waifuviewer.data.db.datasources.FavoriteDataSource
import com.mackenzie.waifuviewer.data.db.datasources.RoomBestDataSource
import com.mackenzie.waifuviewer.data.server.RemoteConnect
import com.mackenzie.waifuviewer.data.server.ServerBestDataSource
import com.mackenzie.waifuviewer.data.server.WaifuBestGif
import com.mackenzie.waifuviewer.data.server.WaifuBestPng
import com.mackenzie.waifuviewer.ui.fakes.*

fun buildBestRepositoryWith(
    localData: List<WaifuBestDbItem>,
    remoteDataPng: List<WaifuBestPng>,
    remoteDataGif: List<WaifuBestGif>
): WaifusBestRepository {
    val localDataSource = RoomBestDataSource(FakeWaifuBestDao(localData))
    val favoriteDataSource = FavoriteDataSource(FakeFavoriteDao())
    val remoteDataSource = ServerBestDataSource( RemoteConnect(
        FakeRemoteImService(listOf()),
        FakeRemotePicsService(listOf()),
        FakeRemoteBestService(remoteDataPng, remoteDataGif)
    )
    )
    return WaifusBestRepository(localDataSource, favoriteDataSource , remoteDataSource)
}

fun buildBestDatabaseWaifus(vararg id: Int) = id.map {
    WaifuBestDbItem(
        id = it,
        artistHref = "",
        artistName = "Overview $it",
        animeName = "Anime Name",
        sourceUrl = "https://cdn.waifu.im/5f7e656343cb7be1.jpg",
        url = "https://cdn.waifu.im/5f7e656343cb7be1.jpg",
        isFavorite = false
    )
}

fun buildBestRemoteWaifus(vararg id: Int) = id.map {
    WaifuBestPng(
        artistHref = "Artis REF",
        artistName = "Overview $it",
        sourceUrl = "https://cdn.waifu.im/5f7e656343cb7be1.jpg",
        url = "https://cdn.waifu.im/5f7e656343cb7be1.jpg"
    )
}