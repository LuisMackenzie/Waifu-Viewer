package com.mackenzie.waifuviewer.data.server

import arrow.core.Either
import com.mackenzie.waifuviewer.data.datasource.WaifusPicRemoteDataSource
import com.mackenzie.waifuviewer.data.tryCall
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.WaifuPicItem
import javax.inject.Inject

class ServerPicDataSource @Inject constructor(private val remoteService: RemoteConnect) : WaifusPicRemoteDataSource {

    override suspend fun getRandomWaifusPics(isNsfw: String, tag: String): Either<Error, List<WaifuPicItem>> = tryCall {
        remoteService.servicePic
            .getRandomWaifuPics(type = isNsfw, category = tag, body = WaifuPicsRequest(isNsfw, tag))
            .body()!!
            .images
            .toDomainModel()
    }

    override suspend fun getOnlyWaifuPics(): WaifuPicItem? {
        val waifu: WaifuPicItem?
        try {
            waifu = remoteService.servicePic
                .getOnlyWaifuPic().url.toDomainModel()
        } catch (e: Exception) {
            return null
        }
        return waifu
    }

}

private fun List<String>.toDomainModel() : List<WaifuPicItem> = map { it.toDomainModel()}

private fun String.toDomainModel(): WaifuPicItem = WaifuPicItem(id = 0, url = this, isFavorite = false)