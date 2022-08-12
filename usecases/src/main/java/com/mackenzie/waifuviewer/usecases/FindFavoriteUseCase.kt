package com.mackenzie.waifuviewer.usecases

import com.mackenzie.waifuviewer.data.FavoritesRepository
import com.mackenzie.waifuviewer.domain.FavoriteItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FindFavoriteUseCase @Inject constructor(private val repo: FavoritesRepository) {

    operator fun invoke(id: Int): Flow<FavoriteItem> = repo.findFavoriteById(id)
}