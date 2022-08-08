package com.mackenzie.waifuviewer.usecases

import arrow.core.Either
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.data.WaifusPicRepository
import com.mackenzie.waifuviewer.domain.WaifuPicItem
import javax.inject.Inject

class RequestMoreWaifuPicUseCase @Inject constructor(private val repo: WaifusPicRepository) {

    suspend operator fun invoke(isNsfw:String, tag:String) : Either<Error, List<WaifuPicItem>> {
        return repo.requestNewWaifusPics(isNsfw, tag)
    }

}