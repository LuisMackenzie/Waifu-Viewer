package com.mackenzie.waifuviewer.usecases

import arrow.core.Either
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.data.WaifusImRepository
import com.mackenzie.waifuviewer.domain.WaifuImItem

class RequestMoreWaifuImUseCase(private val repo: WaifusImRepository) {

    suspend operator fun invoke(isNsfw:Boolean, tag:String, isGif:Boolean, orientation:Boolean): Either<Error?, List<WaifuImItem>> {
        return repo.requestNewWaifusIm(isNsfw, tag, isGif,  orientation)
    }

}