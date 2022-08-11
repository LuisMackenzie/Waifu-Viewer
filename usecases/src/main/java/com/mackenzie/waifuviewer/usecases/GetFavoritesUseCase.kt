package com.mackenzie.waifuviewer.usecases

import com.mackenzie.waifuviewer.data.FavoritesRepository
import javax.inject.Inject

class GetFavoritesUseCase @Inject constructor(private val repo: FavoritesRepository) {
    operator fun invoke() = repo.savedFavorites
}