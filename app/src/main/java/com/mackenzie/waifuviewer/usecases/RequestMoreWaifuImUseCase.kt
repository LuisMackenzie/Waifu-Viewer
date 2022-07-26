package com.mackenzie.waifuviewer.usecases

import com.mackenzie.waifuviewer.data.Error
import com.mackenzie.waifuviewer.data.datasource.WaifusImRepository

class RequestMoreWaifuImUseCase(private val repo: WaifusImRepository) {

    suspend operator fun invoke(isNsfw:Boolean, tag:String, isGif:Boolean, orientation:Boolean): Error?
            = repo.requestNewWaifusIm(isNsfw, tag, isGif,  orientation)

}