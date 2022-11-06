package com.mackenzie.waifuviewer.data.datasource

import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.WaifuBestItem
import kotlinx.coroutines.flow.Flow

interface WaifusBestLocalDataSource {
    val waifus: Flow<List<WaifuBestItem>>
    suspend fun isEmpty(): Boolean
    fun findById(id: Int): Flow<WaifuBestItem>
    suspend fun save(waifus: List<WaifuBestItem>): Error?
    suspend fun saveOnly(waifu: WaifuBestItem): Error?
    suspend fun deleteAll(): Error?
}