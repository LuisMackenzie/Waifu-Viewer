package com.mackenzie.waifuviewer.data.datasource

import com.mackenzie.waifuviewer.domain.WaifuPicItem
import com.mackenzie.waifuviewer.domain.Error
import kotlinx.coroutines.flow.Flow

interface WaifusPicLocalDataSource {
    val waifusPic: Flow<List<WaifuPicItem>>
    suspend fun isPicsEmpty(): Boolean
    fun findPicById(id: Int): Flow<WaifuPicItem>
    suspend fun savePics(waifus: List<WaifuPicItem>): Error?
    suspend fun saveOnlyPics(waifu: WaifuPicItem): Error?
}