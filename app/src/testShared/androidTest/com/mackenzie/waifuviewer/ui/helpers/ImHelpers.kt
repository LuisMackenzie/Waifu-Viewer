package com.mackenzie.waifuviewer.ui

import com.mackenzie.waifuviewer.data.WaifusImRepository
import com.mackenzie.waifuviewer.data.db.WaifuImDbItem
import com.mackenzie.waifuviewer.data.db.datasources.FavoriteDataSource
import com.mackenzie.waifuviewer.data.db.datasources.RoomImDataSource
import com.mackenzie.waifuviewer.data.server.ServerImDataSource
import com.mackenzie.waifuviewer.data.server.models.RemoteConnect
import com.mackenzie.waifuviewer.data.server.models.WaifuIm
import com.mackenzie.waifuviewer.ui.fakes.FakeFavoriteDao
import com.mackenzie.waifuviewer.ui.fakes.FakeRemoteBestService
import com.mackenzie.waifuviewer.ui.fakes.FakeRemoteImService
import com.mackenzie.waifuviewer.ui.fakes.FakeRemotePicsService
import com.mackenzie.waifuviewer.ui.fakes.FakeWaifuImDao
import com.mackenzie.waifuviewer.ui.fakes.FakeWaifuImTagsDao

fun buildImRepositoryWith(
    localData: List<WaifuImDbItem>,
    remoteData: List<WaifuIm>
): WaifusImRepository {
    val localImDataSource = RoomImDataSource(FakeWaifuImDao(localData), FakeWaifuImTagsDao())
    val favoriteDataSource = FavoriteDataSource(FakeFavoriteDao())
    val remoteImDataSource = ServerImDataSource( RemoteConnect(
        FakeRemoteImService(remoteData),
        FakeRemotePicsService(listOf()),
        FakeRemoteBestService(listOf()),
        FakeRemoteMoeService(listOf()),
        FakeRemoteOpenAiService()
    )
    )
    return WaifusImRepository(localImDataSource, favoriteDataSource , remoteImDataSource)
}

fun buildImDatabaseWaifus(vararg id: Int) = id.map {
    WaifuImDbItem(
        id = it,
        artist = "",
        byteSize = 0,
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
        tags = "",
        isFavorite = false
    )
}

fun buildImRemoteWaifus(vararg id: Int) = id.map {
    WaifuIm(
        artist = null,
        byteSize = 0,
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
        previewUrl = "",
        tags = emptyList()
    )
}