package com.mackenzie.waifuviewer.data.datasource

import com.mackenzie.waifuviewer.domain.WaifuImItem
import kotlinx.coroutines.flow.Flow

interface WaifusImLocalDataSource {
    val waifusIm: Flow<List<WaifuImItem>>
    suspend fun isImEmpty(): Boolean
    fun findImById(id: Int): Flow<WaifuImItem>
    suspend fun saveIm(waifus: List<WaifuImItem>)
}