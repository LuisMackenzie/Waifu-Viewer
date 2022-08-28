package com.mackenzie.waifuviewer.ui.fakes

import com.mackenzie.waifuviewer.data.db.FavoriteDao
import com.mackenzie.waifuviewer.data.db.FavoriteDbItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FakeFavoriteDao(waifus: List<FavoriteDbItem> = emptyList()) : FavoriteDao {

    private val inMemoryMovies = MutableStateFlow(waifus)

    private lateinit var findWaifuFlow: MutableStateFlow<FavoriteDbItem>

    override fun getAllFavs(): Flow<List<FavoriteDbItem>> = inMemoryMovies

    override fun findFavById(id: Int): Flow<FavoriteDbItem> {
        findWaifuFlow = MutableStateFlow(inMemoryMovies.value.first { it.id == id })
        return findWaifuFlow
    }

    override suspend fun favoriteCount(): Int = inMemoryMovies.value.size

    override suspend fun insertFavorite(waifu: FavoriteDbItem) {
        inMemoryMovies.update { it }
    }

    override suspend fun insertAllFavorite(waifus: List<FavoriteDbItem>) {
        inMemoryMovies.value = waifus

        if (::findWaifuFlow.isInitialized) {
            waifus.firstOrNull() { it.id == findWaifuFlow.value.id }
                ?.let { findWaifuFlow.value = it }
        }

    }

    override suspend fun updateFav(waifu: FavoriteDbItem) {
        inMemoryMovies.update { it }
    }

    override suspend fun deleteFav(waifu: FavoriteDbItem) {
        inMemoryMovies.update { it }
    }

    override suspend fun deleteAll() {
        inMemoryMovies.update { it }
    }

}