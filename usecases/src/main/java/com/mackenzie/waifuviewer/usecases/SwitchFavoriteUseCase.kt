package com.mackenzie.waifuviewer.usecases

import com.mackenzie.waifuviewer.data.FavoritesRepository
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.FavoriteItem
import javax.inject.Inject

class SwitchFavoriteUseCase @Inject constructor(private val repo: FavoritesRepository) {

    suspend operator fun invoke(waifu: FavoriteItem): Error? = repo.switchFavorite(waifu)
}