package com.mackenzie.waifuviewer.ui

import com.mackenzie.waifuviewer.data.WaifusPicRepository
import com.mackenzie.waifuviewer.data.db.FavoriteDataSource
import com.mackenzie.waifuviewer.data.db.RoomPicDataSource
import com.mackenzie.waifuviewer.data.db.WaifuPicDbItem
import com.mackenzie.waifuviewer.data.server.ServerPicDataSource
import com.mackenzie.waifuviewer.ui.fakes.FakeFavoriteDao
import com.mackenzie.waifuviewer.ui.fakes.FakeRemotePicsService
import com.mackenzie.waifuviewer.ui.fakes.FakeWaifuPicDao

fun buildPicRepositoryWith(
    localData: List<WaifuPicDbItem>,
    remoteData: List<String>
): WaifusPicRepository {
    val localPicDataSource = RoomPicDataSource(FakeWaifuPicDao(localData))
    val favoriteDataSource = FavoriteDataSource(FakeFavoriteDao())
    val remotePicDataSource = ServerPicDataSource( FakeRemotePicsService(remoteData))
    return WaifusPicRepository(localPicDataSource, favoriteDataSource , remotePicDataSource)
}

fun buildDatabasePicMovies(vararg id: Int) = id.map {
    WaifuPicDbItem(
        id = it,
        url = "https://cdn.waifu.im/5f7e656343cb7be1.jpg",
        isFavorite = false
    )
}

fun buildRemotePicMovies(vararg id: Int) = id.map {
    "https://cdn.waifu.im/5f7e656343cb7be1.jpg"
}