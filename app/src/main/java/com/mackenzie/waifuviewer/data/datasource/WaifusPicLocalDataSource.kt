package com.mackenzie.waifuviewer.data.datasource

import com.mackenzie.waifuviewer.domain.WaifuPicItem
import kotlinx.coroutines.flow.Flow

interface WaifusPicLocalDataSource {
    val waifusPic: Flow<List<WaifuPicItem>>
    suspend fun isPicsEmpty(): Boolean
    fun findPicById(id: Int): Flow<WaifuPicItem>
    suspend fun savePics(waifus: List<WaifuPicItem>)
}