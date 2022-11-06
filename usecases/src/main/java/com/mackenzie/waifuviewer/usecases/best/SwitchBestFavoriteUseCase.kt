package com.mackenzie.waifuviewer.usecases.best

import com.mackenzie.waifuviewer.data.WaifusBestRepository
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.WaifuBestItem
import javax.inject.Inject

class SwitchBestFavoriteUseCase @Inject constructor(private val repo: WaifusBestRepository) {

    suspend operator fun invoke(waifu: WaifuBestItem): Error? = repo.switchFavorite(waifu)
}