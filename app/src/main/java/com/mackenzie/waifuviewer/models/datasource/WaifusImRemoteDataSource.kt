package com.mackenzie.waifuviewer.models.datasource

import com.google.gson.JsonObject
import com.mackenzie.waifuviewer.models.RemoteConnection

class WaifusImRemoteDataSource {

    suspend fun getRandomWaifusIm(isNsfw: Boolean, tag: String, isGif: Boolean, orientation: String) =
        RemoteConnection.serviceIm.getRandomWaifu(isNsfw, tag, isGif,  orientation)

}