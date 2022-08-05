package com.mackenzie.waifuviewer.usecases

import com.mackenzie.waifuviewer.data.WaifusImRepository

class RequestOnlyWaifuImUseCase(private val repo: WaifusImRepository) {

    suspend operator fun invoke() = repo.requestOnlyWaifuIm()

    // suspend fun get() = repo.requestOnlyWaifuIm()

}

