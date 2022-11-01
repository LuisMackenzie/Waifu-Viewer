package com.mackenzie.waifuviewer.data.datasource

import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.WaifuBestItemPng
import kotlinx.coroutines.flow.Flow

interface WaifusBestPngLocalDataSource {
    val waifusPng: Flow<List<WaifuBestItemPng>>
    suspend fun isPngEmpty(): Boolean
    fun findPngById(id: Int): Flow<WaifuBestItemPng>
    suspend fun savePng(waifus: List<WaifuBestItemPng>): Error?
    suspend fun saveOnlyPng(waifu: WaifuBestItemPng): Error?
    suspend fun deleteAllPng(): Error?
}