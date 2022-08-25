package com.mackenzie.waifuviewer.ui

import com.mackenzie.waifuviewer.data.RegionRepository
import com.mackenzie.waifuviewer.data.WaifusImRepository
import com.mackenzie.waifuviewer.data.WaifusPicRepository
import com.mackenzie.waifuviewer.data.db.*
import com.mackenzie.waifuviewer.data.server.ServerImDataSource
import com.mackenzie.waifuviewer.data.server.ServerPicDataSource
import com.mackenzie.waifuviewer.data.server.Waifu
import com.mackenzie.waifuviewer.data.server.WaifuPic
import com.mackenzie.waifuviewer.ui.fakes.*

fun buildImRepositoryWith(
    localData: List<WaifuImDbItem>,
    remoteData: List<Waifu>
): WaifusImRepository {
    val locationDataSource = FakeLocationDataSource()
    val permissionChecker = FakePermissionChecker()
    val regionRepository = RegionRepository(locationDataSource, permissionChecker)
    val localImDataSource = RoomImDataSource(FakeWaifuImDao(localData))
    val favoriteDataSource = FavoriteDataSource(FakeFavoriteDao())
    val remoteImDataSource = ServerImDataSource( FakeRemoteImService(remoteData))
    return WaifusImRepository(localImDataSource, favoriteDataSource , remoteImDataSource)
}

fun buildPicRepositoryWith(
    localData: List<WaifuPicDbItem>,
    remoteData: List<WaifuPic>
): WaifusPicRepository {
    val locationDataSource = FakeLocationDataSource()
    val permissionChecker = FakePermissionChecker()
    val regionRepository = RegionRepository(locationDataSource, permissionChecker)
    val localPicDataSource = RoomPicDataSource(FakeWaifuPicDao(localData))
    val favoriteDataSource = FavoriteDataSource(FakeFavoriteDao())
    val remotePicDataSource = ServerPicDataSource( FakeRemotePicsService(remoteData))
    return WaifusPicRepository(localPicDataSource, favoriteDataSource , remotePicDataSource)
}

fun buildDatabaseMovies(vararg id: Int) = id.map {
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

fun buildRemoteMovies(vararg id: Int) = id.map {
    Waifu(
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