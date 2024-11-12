package com.mackenzie.waifuviewer.data.server

import arrow.core.Either
import com.mackenzie.waifuviewer.data.datasource.WaifusImRemoteDataSource
import com.mackenzie.waifuviewer.data.server.mapper.toDomainModel
import com.mackenzie.waifuviewer.data.server.models.RemoteConnect
import com.mackenzie.waifuviewer.data.tryCall
import com.mackenzie.waifuviewer.data.trySave
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.im.WaifuImItem
import com.mackenzie.waifuviewer.domain.im.WaifuImTagList
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

    override suspend fun getWaifuImTags(): WaifuImTagList? = trySave {
        remoteService.serviceIm
            .getTagsWaifuIm()
            .toDomainModel()
    }

}


