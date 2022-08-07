package com.mackenzie.waifuviewer.data.server

import arrow.core.Either
import arrow.core.right
import com.mackenzie.waifuviewer.data.datasource.WaifusImRemoteDataSource
import com.mackenzie.waifuviewer.data.server.RemoteConnection.serviceIm
import com.mackenzie.waifuviewer.data.tryCall
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.WaifuImItem
import javax.inject.Inject


class ServerImDataSource @Inject constructor(): WaifusImRemoteDataSource {

    override suspend fun getRandomWaifusIm(isNsfw: Boolean, tag: String, isGif: Boolean, orientation: String): Either<Error? , List<WaifuImItem>> = tryCall {
        serviceIm.getRandomWaifuIm(isNsfw, tag, isGif,  orientation)
            .waifus
            .toDomainModel()
    }

    override suspend fun getOnlyWaifuIm(): WaifuImItem {
        val waifu = serviceIm.getOnlyRandomWaifuIm().waifus.first().toDomainModel()
        return waifu
    }

}

private fun List<Waifu>.toDomainModel(): List<WaifuImItem> = map { it.toDomainModel() }

private fun Waifu.toDomainModel(): WaifuImItem =
    WaifuImItem(
        id,
        dominant_color,
        file,
        height,
        imageId,
        isNsfw,
        url,
        width,
        isFavorite = false
    )