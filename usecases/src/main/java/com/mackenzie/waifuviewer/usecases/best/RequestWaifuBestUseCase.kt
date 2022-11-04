package com.mackenzie.waifuviewer.usecases.best

import com.mackenzie.waifuviewer.data.WaifusBestRepository
import com.mackenzie.waifuviewer.domain.Error
import javax.inject.Inject

class RequestWaifuBestUseCase @Inject constructor(private val repo: WaifusBestRepository) {

    suspend operator fun invoke(isGif:Boolean, tag:String): Error? {
        if (isGif) {
            return repo.requestWaifusGif(tag)
        } else {
            return repo.requestWaifusPng(tag)
        }
    }
}