package com.mackenzie.waifuviewer.data.server

import arrow.core.Either
import com.google.gson.JsonObject
import com.mackenzie.waifuviewer.data.datasource.WaifusPicRemoteDataSource
import com.mackenzie.waifuviewer.data.server.RemoteConnection.serviceIm
import com.mackenzie.waifuviewer.data.server.RemoteConnection.servicePics
import com.mackenzie.waifuviewer.data.tryCall
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.WaifuImItem
import com.mackenzie.waifuviewer.domain.WaifuPicItem
import javax.inject.Inject

class ServerPicDataSource @Inject constructor() : WaifusPicRemoteDataSource {

    override suspend fun getRandomWaifusPics(isNsfw: String, tag: String): Either<Error, List<WaifuPicItem>> = tryCall {
        servicePics
            .getRandomWaifuPics(type = isNsfw, category = tag, body = getJson(isNsfw, tag))
            .body()!!
            .images
            .toDomainModel()
    }

    override suspend fun getOnlyWaifuPics(): WaifuPicItem? {
        var waifu: WaifuPicItem? = null
        try {
            waifu = servicePics.getOnlyWaifuPic().url.toDomainModel()
        } catch (e: Exception) {
            return null
        } finally {
            if (waifu != null) return waifu else return null
        }
    }

    /*override suspend fun getOnlyWaifuPics(): WaifuPicItem {
        val waifu = servicePics.getOnlyWaifuPic().url.toDomainModel()
        return waifu
    }*/



    private fun getJson(isNsfw: String, tag: String): JsonObject {
        val jsonBody = JsonObject()
        jsonBody.addProperty("classification", isNsfw)
        jsonBody.addProperty("category", tag)
        return jsonBody
    }

}

private fun List<String>.toDomainModel() : List<WaifuPicItem> = map { it.toDomainModel()}

private fun String.toDomainModel(): WaifuPicItem = WaifuPicItem(id = 0, url = this, isFavorite = false)