package com.mackenzie.waifuviewer.data.datasource

import com.mackenzie.waifuviewer.domain.WaifuPicItem

interface WaifusPicRemoteDataSource {
    suspend fun getRandomWaifusPics(isNsfw: String, tag: String): List<WaifuPicItem>
}