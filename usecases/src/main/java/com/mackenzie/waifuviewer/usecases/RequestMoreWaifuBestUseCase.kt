package com.mackenzie.waifuviewer.usecases

import com.mackenzie.waifuviewer.data.WaifusBestRepository
import com.mackenzie.waifuviewer.domain.Error
import javax.inject.Inject

class RequestMoreWaifuBestUseCase @Inject constructor(private val repo: WaifusBestRepository) {

    suspend operator fun invoke(isGif:Boolean, tag:String): Error? {
        if (isGif) {
            return repo.requestNewWaifusGif(tag)
        } else {
            return repo.requestNewWaifusPng(tag)
        }

    }
}