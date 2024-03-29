package com.mackenzie.waifuviewer.usecases.best

import com.mackenzie.waifuviewer.data.WaifusBestRepository
import com.mackenzie.waifuviewer.domain.Error
import javax.inject.Inject

class RequestMoreWaifuBestUseCase @Inject constructor(private val repo: WaifusBestRepository) {

    suspend operator fun invoke(tag:String): Error? {
        return repo.requestNewWaifus(tag)
    }
}