package com.mackenzie.waifuviewer.usecases.best

import com.mackenzie.waifuviewer.data.WaifusBestRepository
import com.mackenzie.waifuviewer.domain.Error
import javax.inject.Inject

class ClearWaifuBestUseCase @Inject constructor(private val repo: WaifusBestRepository) {

    suspend operator fun invoke(isGif:Boolean): Error? {
        if (isGif) {
            return repo.requestClearWaifusGif()
        } else {
            return repo.requestClearWaifusPng()
        }
    }
}