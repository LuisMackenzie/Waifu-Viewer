package com.mackenzie.waifuviewer.usecases

import com.mackenzie.waifuviewer.data.WaifusBestRepository
import javax.inject.Inject

class GetWaifuGifUseCase @Inject constructor(private val repo: WaifusBestRepository) {
    operator fun invoke() = repo.savedWaifusGif
}