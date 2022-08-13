package com.mackenzie.waifuviewer.data.datasource

import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.FavoriteItem
import com.mackenzie.waifuviewer.domain.WaifuImItem
import com.mackenzie.waifuviewer.domain.WaifuPicItem
import kotlinx.coroutines.flow.Flow

interface FavoriteLocalDataSource {
    val favoriteWaifus: Flow<List<FavoriteItem>>
    suspend fun isFavEmpty(): Boolean
    fun findFavById(id: Int): Flow<FavoriteItem>
    suspend fun saveIm(waifu: WaifuImItem): Error?
    suspend fun savePic(waifu: WaifuPicItem): Error?
    suspend fun save(waifu: FavoriteItem): Error?
    suspend fun deleteIm(waifu: WaifuImItem): Error?
    suspend fun deletePic(waifu: WaifuPicItem): Error?
    suspend fun delete(waifu: FavoriteItem): Error?
    suspend fun deleteAll(): Error?
}