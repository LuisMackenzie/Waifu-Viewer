package com.mackenzie.waifuviewer.data.datasource

import com.mackenzie.waifuviewer.domain.im.WaifuImItem
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.im.WaifuImTagList
import kotlinx.coroutines.flow.Flow

interface WaifusImLocalDataSource {
    val waifusIm: Flow<List<WaifuImItem>>
    val waifuImTags: Flow<WaifuImTagList>
    suspend fun isImEmpty(): Boolean
    suspend fun isTagsImEmpty(): Boolean
    fun findImById(id: Int): Flow<WaifuImItem>
    suspend fun saveIm(waifus: List<WaifuImItem>): Error?
    suspend fun saveImTags(tags: WaifuImTagList): Error?
    suspend fun saveOnlyIm(waifu: WaifuImItem): Error?
    suspend fun deleteAll(): Error?
}