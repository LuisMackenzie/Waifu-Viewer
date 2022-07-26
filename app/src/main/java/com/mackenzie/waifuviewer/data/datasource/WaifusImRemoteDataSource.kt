package com.mackenzie.waifuviewer.data.datasource

import com.mackenzie.waifuviewer.data.WaifuResult
import com.mackenzie.waifuviewer.domain.WaifuImItem

interface WaifusImRemoteDataSource {
    suspend fun getRandomWaifusIm(isNsfw: Boolean, tag: String, isGif: Boolean, orientation: String): List<WaifuImItem>
}