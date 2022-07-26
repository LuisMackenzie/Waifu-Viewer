package com.mackenzie.waifuviewer.framework.server

import com.google.gson.JsonObject
import com.mackenzie.waifuviewer.data.datasource.WaifusPicRemoteDataSource
import com.mackenzie.waifuviewer.domain.WaifuPicItem

class ServerPicDataSource : WaifusPicRemoteDataSource {

    override suspend fun getRandomWaifusPics(isNsfw: String, tag: String): List<WaifuPicItem> =
        RemoteConnection.servicePics
            .getWaifuPics(type = isNsfw, category = tag, body = getJson(isNsfw, tag))
            .body()!!
            .images
            .toDomainModel()


    private fun getJson(isNsfw: String, tag: String): JsonObject {
        val jsonBody = JsonObject()
        jsonBody.addProperty("classification", isNsfw)
        jsonBody.addProperty("category", tag)
        return jsonBody
    }

}

private fun List<String>.toDomainModel() : List<WaifuPicItem> = map { it.toDomainModel() }

private fun String.toDomainModel(): WaifuPicItem = WaifuPicItem(id = 0, url = this, isFavorite = false)