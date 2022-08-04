package com.mackenzie.waifuviewer.usecases

import arrow.core.Either
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.data.WaifusPicRepository
import com.mackenzie.waifuviewer.domain.WaifuPicItem

class RequestOnlyWaifuPicUseCase (private val repo: WaifusPicRepository){

    suspend operator fun invoke() = repo.requestOnlyWaifuPic()

    // suspend fun get(): Either<Error, WaifuPicItem> = repo.requestOnlyWaifuPic()

}