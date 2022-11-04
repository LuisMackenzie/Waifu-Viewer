package com.mackenzie.waifuviewer.usecases.im

import com.mackenzie.waifuviewer.data.WaifusImRepository
import com.mackenzie.waifuviewer.domain.Error
import javax.inject.Inject

class RequestWaifuImUseCase @Inject constructor(private val repo: WaifusImRepository) {

    suspend operator fun invoke(isNsfw:Boolean, tag:String, isGif:Boolean, orientation:Boolean): Error? {
        return repo.requestWaifusIm(isNsfw, tag, isGif,  orientation)
    }

}