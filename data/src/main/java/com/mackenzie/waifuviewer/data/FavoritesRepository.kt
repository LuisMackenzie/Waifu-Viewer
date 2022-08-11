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

    fun findImById(id: Int): Flow<FavoriteItem> = favDataSource.findFavById(id)

    suspend fun switchImFavorite(imItem: FavoriteItem): Error? {
        val updatedWaifu = imItem.copy(isFavorite = !imItem.isFavorite)
        return favDataSource.save(updatedWaifu)
    }

}