package com.mackenzie.waifuviewer.data.datasource

import com.mackenzie.waifuviewer.domain.WaifuImItem
import com.mackenzie.waifuviewer.domain.Error
import kotlinx.coroutines.flow.Flow

interface WaifusImLocalDataSource {
    val waifusIm: Flow<List<WaifuImItem>>
    // val waifusImPaged: Flow<PagingData<WaifuImItem>>
    suspend fun isImEmpty(): Boolean
    fun findImById(id: Int): Flow<WaifuImItem>
    suspend fun saveIm(waifus: List<WaifuImItem>): Error?
    suspend fun saveOnlyIm(waifu: WaifuImItem): Error?
    suspend fun deleteAll(): Error?
}