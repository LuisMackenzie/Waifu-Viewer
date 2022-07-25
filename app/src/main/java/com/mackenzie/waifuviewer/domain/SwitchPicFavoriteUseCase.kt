package com.mackenzie.waifuviewer.domain

import com.mackenzie.waifuviewer.data.datasource.WaifusPicRepository
import com.mackenzie.waifuviewer.data.db.WaifuPicItem
import com.mackenzie.waifuviewer.data.Error

class SwitchPicFavoriteUseCase(private val repo: WaifusPicRepository) {

    suspend operator fun invoke(waifu: WaifuPicItem): Error? = repo.switchPicsFavorite(waifu)

}