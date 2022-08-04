package com.mackenzie.waifuviewer.usecases

import com.mackenzie.waifuviewer.data.WaifusPicRepository
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.WaifuPicItem

class SwitchPicFavoriteUseCase(private val repo: WaifusPicRepository) {

    suspend operator fun invoke(waifu: WaifuPicItem): Error? = repo.switchPicsFavorite(waifu)

}