package com.mackenzie.waifuviewer.data.datasource

import com.google.gson.JsonObject
import com.mackenzie.waifuviewer.data.RemoteConnection

class WaifusPicRemoteDataSource {

    suspend fun getRandomWaifusPics(isNsfw: String, tag: String) =
        RemoteConnection.servicePics.getWaifuPics(type = isNsfw, category = tag, body = getJson(isNsfw, tag)).body()!!.images


    private fun getJson(isNsfw: String, tag: String): JsonObject {
        val jsonBody = JsonObject()
        jsonBody.addProperty("classification", isNsfw)
        jsonBody.addProperty("category", tag)
        return jsonBody
    }

}