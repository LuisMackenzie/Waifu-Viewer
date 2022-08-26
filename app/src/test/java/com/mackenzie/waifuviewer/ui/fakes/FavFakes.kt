package com.mackenzie.waifuviewer.ui.fakes

import com.mackenzie.waifuviewer.data.db.FavoriteDao
import com.mackenzie.waifuviewer.data.db.FavoriteDbItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow


class FakeFavoriteDao(waifusIm: List<FavoriteDbItem> = emptyList()) : FavoriteDao {

    private val inMemoryMovies = MutableStateFlow(waifusIm)

    private lateinit var findWaifuFlow: MutableStateFlow<FavoriteDbItem>

    override fun getAllFavs(): Flow<List<FavoriteDbItem>> = inMemoryMovies

    override fun findFavById(id: Int): Flow<FavoriteDbItem> {
        findWaifuFlow = MutableStateFlow(inMemoryMovies.value.first { it.id == id })
        return findWaifuFlow
    }

    override suspend fun favoriteCount(): Int = inMemoryMovies.value.size

    override suspend fun insertFavorite(waifu: FavoriteDbItem) {
        TODO("Not yet implemented")
    }

    override suspend fun insertAllFavorite(waifus: List<FavoriteDbItem>) {
        inMemoryMovies.value = waifus

        if (::findWaifuFlow.isInitialized) {
            waifus.firstOrNull() { it.id == findWaifuFlow.value.id }
                ?.let { findWaifuFlow.value = it }
        }

    }

    override suspend fun updateFav(waifu: FavoriteDbItem) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteFav(waifu: FavoriteDbItem) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAll() {
        TODO("Not yet implemented")
    }

}
/*
class FakeFavoriteDataSource : FavoriteLocalDataSource {

    val inMemoryWaifus = MutableStateFlow<List<FavoriteItem>>(emptyList())

    override val favoriteWaifus = inMemoryWaifus

    private lateinit var findWaifuFlow: MutableStateFlow<FavoriteItem>

    override suspend fun isFavEmpty() = favoriteWaifus.value.isEmpty()

    override fun findFavById(id: Int): Flow<FavoriteItem> {
        findWaifuFlow = MutableStateFlow(inMemoryWaifus.value.first { it.id == id })
        return findWaifuFlow
    }

    override suspend fun updateIm(waifu: WaifuImItem): Error? {
        return null
    }

    override suspend fun updatePic(waifu: WaifuImItem): Error? {
        return null
    }

    override suspend fun save(waifu: FavoriteItem): Error? {
        return null
    }

    override suspend fun saveIm(waifu: WaifuImItem): Error? {
        return null
    }

    override suspend fun savePic(waifu: WaifuPicItem): Error? {
        return null
    }

    override suspend fun deleteIm(waifu: WaifuImItem): Error? {
        return null
    }

    override suspend fun deletePic(waifu: WaifuPicItem): Error? {
        return null
    }

    override suspend fun delete(waifu: FavoriteItem): Error? {
        return null
    }

    override suspend fun deleteAll(): Error? {
        return null
    }
}*/
