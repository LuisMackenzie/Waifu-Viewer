package com.mackenzie.waifuviewer.usecases

import com.mackenzie.waifuviewer.data.datasource.WaifusPicRepository
import com.mackenzie.waifuviewer.domain.WaifuPicItem
import com.mackenzie.waifuviewer.domain.Error

class SwitchPicFavoriteUseCase(private val repo: WaifusPicRepository) {

    suspend operator fun invoke(waifu: WaifuPicItem): Error? = repo.switchPicsFavorite(waifu)

}