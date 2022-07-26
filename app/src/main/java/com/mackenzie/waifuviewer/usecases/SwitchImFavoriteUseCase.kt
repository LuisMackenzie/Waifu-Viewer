package com.mackenzie.waifuviewer.usecases

import com.mackenzie.waifuviewer.data.datasource.WaifusImRepository
import com.mackenzie.waifuviewer.domain.WaifuImItem
import com.mackenzie.waifuviewer.data.Error

class SwitchImFavoriteUseCase(private val repo: WaifusImRepository) {

    suspend operator fun invoke(waifu: WaifuImItem): Error? = repo.switchImFavorite(waifu)

}