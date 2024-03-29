package com.mackenzie.waifuviewer.usecases.pics

import com.mackenzie.waifuviewer.data.WaifusPicRepository
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.WaifuPicItem
import javax.inject.Inject

class SwitchPicFavoriteUseCase @Inject constructor(private val repo: WaifusPicRepository) {

    suspend operator fun invoke(waifu: WaifuPicItem): Error? = repo.switchPicsFavorite(waifu)

}