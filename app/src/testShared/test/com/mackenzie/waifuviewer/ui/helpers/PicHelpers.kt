package com.mackenzie.waifuviewer.ui

import com.mackenzie.waifuviewer.data.WaifusPicRepository
import com.mackenzie.waifuviewer.data.db.*
import com.mackenzie.waifuviewer.data.db.datasources.FavoriteDataSource
import com.mackenzie.waifuviewer.data.db.datasources.RoomPicDataSource
import com.mackenzie.waifuviewer.data.server.*
import com.mackenzie.waifuviewer.ui.fakes.*

fun buildPicRepositoryWith(
    localData: List<WaifuPicDbItem>,
    remoteData: List<String>
): WaifusPicRepository {
    val localPicDataSource = RoomPicDataSource(FakeWaifuPicDao(localData))
    val favoriteDataSource = FavoriteDataSource(FakeFavoriteDao())
    val remotePicDataSource = ServerPicDataSource( RemoteConnect(
        FakeRemoteImService(listOf()),
        FakeRemotePicsService(remoteData),
        FakeRemoteBestService(listOf()),
        FakeRemoteMoeService(listOf())))
    return WaifusPicRepository(localPicDataSource, favoriteDataSource , remotePicDataSource)
}

fun buildPicDatabaseWaifus(vararg id: Int) = id.map {
    WaifuPicDbItem(
        id = it,
        url = "https://cdn.waifu.im/5f7e656343cb7be1.jpg",
        isFavorite = false
    )
}

fun buildPicRemoteWaifus(vararg id: Int) = id.map {
    "https://cdn.waifu.im/5f7e656343cb7be1.jpg"
}