package com.mackenzie.waifuviewer.usecases

import com.mackenzie.waifuviewer.data.datasource.WaifusImRepository
import com.mackenzie.waifuviewer.domain.Error

class RequestWaifuImUseCase(private val repo: WaifusImRepository) {

    suspend operator fun invoke(isNsfw:Boolean, tag:String, isGif:Boolean, orientation:Boolean): Error?
    = repo.requestWaifusIm(isNsfw, tag, isGif,  orientation)

}