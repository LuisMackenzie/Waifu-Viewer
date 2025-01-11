package com.mackenzie.waifuviewer.ui.fakes

import com.mackenzie.waifuviewer.data.db.dao.FavoriteDao
import com.mackenzie.waifuviewer.data.db.FavoriteDbItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FakeFavoriteDao(waifus: List<FavoriteDbItem> = emptyList()) : FavoriteDao {

    private val inMemoryWaifus = MutableStateFlow(waifus)

    private lateinit var findWaifuFlow: MutableStateFlow<FavoriteDbItem>

    override fun getAllFavs(): Flow<List<FavoriteDbItem>> = inMemoryWaifus

    override fun findFavById(id: Int): Flow<FavoriteDbItem> {
        findWaifuFlow = MutableStateFlow(inMemoryWaifus.value.first { it.id == id })
        return findWaifuFlow
    }

    override suspend fun favoriteCount(): Int = inMemoryWaifus.value.size

    override suspend fun insertFavorite(waifu: FavoriteDbItem) {
        inMemoryWaifus.update { it }
    }

    override suspend fun insertAllFavorite(waifus: List<FavoriteDbItem>) {
        inMemoryWaifus.value = waifus

        if (::findWaifuFlow.isInitialized) {
            waifus.firstOrNull() { it.id == findWaifuFlow.value.id }
                ?.let { findWaifuFlow.value = it }
        }

    }

    override suspend fun updateFav(waifu: FavoriteDbItem) {
        inMemoryWaifus.update { it }
    }

    override suspend fun deleteFav(waifu: FavoriteDbItem) {
        inMemoryWaifus.update { it }
    }

    override suspend fun deleteAll() {
        inMemoryWaifus.update { it }
    }

}