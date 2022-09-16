package com.mackenzie.waifuviewer.data

import com.mackenzie.waifuviewer.data.datasource.FavoriteLocalDataSource
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.FavoriteItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavoritesRepository @Inject constructor(
    private val favDataSource: FavoriteLocalDataSource
    ) {

    val savedFavorites = favDataSource.favoriteWaifus

    fun findFavoriteById(id: Int): Flow<FavoriteItem> = favDataSource.findFavById(id)

    suspend fun saveFavorite(favoriteItem: FavoriteItem): Error? {
        return favDataSource.save(favoriteItem)
    }

    suspend fun deleteFavorite(favoriteItem: FavoriteItem): Error? {
        return favDataSource.delete(favoriteItem)
    }

    suspend fun switchFavorite(favoriteItem: FavoriteItem): Error? {
        val updatedWaifu = favoriteItem.copy(isFavorite = !favoriteItem.isFavorite)
        return favDataSource.save(updatedWaifu)
    }


}