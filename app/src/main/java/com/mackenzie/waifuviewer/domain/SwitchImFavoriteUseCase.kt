package com.mackenzie.waifuviewer.domain

import com.mackenzie.waifuviewer.data.datasource.WaifusImRepository
import com.mackenzie.waifuviewer.data.db.WaifuImItem
import com.mackenzie.waifuviewer.data.Error

class SwitchImFavoriteUseCase(private val repo: WaifusImRepository) {

    suspend operator fun invoke(waifu: WaifuImItem): Error? = repo.switchImFavorite(waifu)

}