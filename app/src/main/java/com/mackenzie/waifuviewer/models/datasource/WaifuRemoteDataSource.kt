package com.mackenzie.waifuviewer.models.datasource

import com.google.gson.JsonObject
import com.mackenzie.waifuviewer.models.RemoteConnection

class WaifusRemoteDataSource {

    suspend fun getRandomWaifusIm(isNsfw: Boolean, tag: String, isGif: Boolean, orientation: String) =
        RemoteConnection.serviceIm.getRandomWaifu(isNsfw, tag, isGif,  orientation)

    suspend fun getRandomWaifusPics(isNsfw: String, tag: String) =
        RemoteConnection.servicePics.getWaifuPics(type = isNsfw, category = tag, body = getJson(isNsfw, tag)).body()!!.images


    private fun getJson(isNsfw: String, tag: String): JsonObject {
        val jsonBody = JsonObject()
        jsonBody.addProperty("classification", isNsfw)
        jsonBody.addProperty("category", tag)
        return jsonBody
    }

}