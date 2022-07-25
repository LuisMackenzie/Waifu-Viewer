package com.mackenzie.waifuviewer.data.datasource

import com.mackenzie.waifuviewer.data.RemoteConnection

class WaifusImRemoteDataSource {

    suspend fun getRandomWaifusIm(isNsfw: Boolean, tag: String, isGif: Boolean, orientation: String) =
        RemoteConnection.serviceIm.getRandomWaifu(isNsfw, tag, isGif,  orientation)

}