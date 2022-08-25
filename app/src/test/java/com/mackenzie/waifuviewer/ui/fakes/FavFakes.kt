package com.mackenzie.waifuviewer.ui.fakes

import com.mackenzie.waifuviewer.data.datasource.FavoriteLocalDataSource
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.FavoriteItem
import com.mackenzie.waifuviewer.domain.WaifuImItem
import com.mackenzie.waifuviewer.domain.WaifuPicItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

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
}