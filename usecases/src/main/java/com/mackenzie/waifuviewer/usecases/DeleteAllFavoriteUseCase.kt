package com.mackenzie.waifuviewer.usecases

import com.mackenzie.waifuviewer.data.FavoritesRepository
import com.mackenzie.waifuviewer.domain.Error
import javax.inject.Inject

class DeleteAllFavoriteUseCase @Inject constructor(private val repo: FavoritesRepository) {

    suspend operator fun invoke(): Error? = repo.deleteAllFavorites()
}