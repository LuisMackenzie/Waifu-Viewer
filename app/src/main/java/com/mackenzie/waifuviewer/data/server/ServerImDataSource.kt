package com.mackenzie.waifuviewer.data.server

import arrow.core.Either
import com.mackenzie.waifuviewer.data.datasource.WaifusImRemoteDataSource
import com.mackenzie.waifuviewer.data.tryCall
import com.mackenzie.waifuviewer.data.trySave
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.WaifuImItem
import java.io.IOException
import javax.inject.Inject


class ServerImDataSource @Inject constructor(private val remoteService: RemoteConnect): WaifusImRemoteDataSource {

    override suspend fun getRandomWaifusIm(
        isNsfw: Boolean, tag: String,
        isGif: Boolean, orientation: String
    ): Either<Error, List<WaifuImItem>> = tryCall {
        remoteService.serviceIm
            .getRandomWaifuIm(isNsfw, tag, isGif,  orientation)
            .waifus
            .toDomainModel()
    }

    override suspend fun getOnlyWaifuIm(orientation: String): WaifuImItem? = trySave {
        remoteService.serviceIm
            .getOnlyRandomWaifuIm(orientation = orientation)
            .waifus
            .firstOrNull()?.toDomainModel()
    }

}

private fun List<WaifuIm>.toDomainModel(): List<WaifuImItem> = map { it.toDomainModel() }

private fun WaifuIm.toDomainModel(): WaifuImItem =
    WaifuImItem(
        0,
        signature,
        extension,
        dominant_color,
        source ?: "",
        uploadedAt,
        isNsfw,
        width,
        height,
        imageId,
        url,
        previewUrl,
        false
    )
