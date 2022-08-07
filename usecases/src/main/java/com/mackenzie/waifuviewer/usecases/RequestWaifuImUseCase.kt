package com.mackenzie.waifuviewer.usecases

import arrow.core.Either
import com.mackenzie.waifuviewer.data.WaifusImRepository
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.WaifuImItem
import javax.inject.Inject

class RequestWaifuImUseCase @Inject constructor(private val repo: WaifusImRepository) {

    suspend operator fun invoke(isNsfw:Boolean, tag:String, isGif:Boolean, orientation:Boolean): Either<Error?, List<WaifuImItem>> {
        return repo.requestWaifusIm(isNsfw, tag, isGif,  orientation)
    }

}