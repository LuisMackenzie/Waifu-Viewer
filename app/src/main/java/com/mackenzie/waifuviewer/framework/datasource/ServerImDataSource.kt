package com.mackenzie.waifuviewer.framework.datasource

import com.mackenzie.waifuviewer.data.RemoteConnection
import com.mackenzie.waifuviewer.data.Waifu
import com.mackenzie.waifuviewer.data.datasource.WaifusImRemoteDataSource
import com.mackenzie.waifuviewer.domain.WaifuImItem

class ServerImDataSource : WaifusImRemoteDataSource {

    override suspend fun getRandomWaifusIm(isNsfw: Boolean, tag: String, isGif: Boolean, orientation: String): List<WaifuImItem> =
        RemoteConnection.serviceIm.getRandomWaifu(isNsfw, tag, isGif,  orientation).waifus.toDomainModel()

}

private fun List<Waifu>.toDomainModel(): List<WaifuImItem> = map { it.toDomainModel() }

private fun Waifu.toDomainModel(): WaifuImItem = WaifuImItem(
    0,
    dominant_color,
    file,
    height,
    imageId,
    isNsfw,
    url,
    width,
    isFavorite = false
)