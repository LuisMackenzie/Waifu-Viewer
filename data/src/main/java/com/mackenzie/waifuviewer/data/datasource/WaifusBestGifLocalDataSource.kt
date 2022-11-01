package com.mackenzie.waifuviewer.data.datasource

import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.WaifuBestItemGif
import kotlinx.coroutines.flow.Flow

interface WaifusBestGifLocalDataSource {
    val waifusGif: Flow<List<WaifuBestItemGif>>
    suspend fun isGifEmpty(): Boolean
    fun findGifById(id: Int): Flow<WaifuBestItemGif>
    suspend fun saveGif(waifus: List<WaifuBestItemGif>): Error?
    suspend fun saveOnlyGif(waifu: WaifuBestItemGif): Error?
    suspend fun deleteAllGif(): Error?
}