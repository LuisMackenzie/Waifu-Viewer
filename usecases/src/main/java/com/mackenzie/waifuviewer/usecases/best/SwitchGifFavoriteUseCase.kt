package com.mackenzie.waifuviewer.usecases.best

import com.mackenzie.waifuviewer.data.WaifusBestRepository
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.WaifuBestItemGif
import javax.inject.Inject

class SwitchGifFavoriteUseCase @Inject constructor(private val repo: WaifusBestRepository) {

    suspend operator fun invoke(waifu: WaifuBestItemGif): Error? = repo.switchGifFavorite(waifu)
}