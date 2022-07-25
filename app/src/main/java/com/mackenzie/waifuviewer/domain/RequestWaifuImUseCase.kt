package com.mackenzie.waifuviewer.domain

import com.mackenzie.waifuviewer.data.datasource.WaifusImRepository
import com.mackenzie.waifuviewer.data.Error

class RequestWaifuImUseCase(private val repo: WaifusImRepository) {

    suspend operator fun invoke(isNsfw:Boolean, tag:String, isGif:Boolean, orientation:Boolean): Error?
    = repo.requestWaifusIm(isNsfw, tag, isGif,  orientation)

}