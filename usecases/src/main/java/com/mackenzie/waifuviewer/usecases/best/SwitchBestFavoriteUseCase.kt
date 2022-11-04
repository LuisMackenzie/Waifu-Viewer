package com.mackenzie.waifuviewer.usecases.best

import com.mackenzie.waifuviewer.data.WaifusBestRepository
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.WaifuBestItemPng
import javax.inject.Inject

class SwitchBestFavoriteUseCase @Inject constructor(private val repo: WaifusBestRepository) {
    suspend operator fun invoke(waifu: WaifuBestItemPng): Error? = repo.switchPngFavorite(waifu)
}