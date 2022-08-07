package com.mackenzie.waifuviewer.usecases

import com.mackenzie.waifuviewer.data.WaifusImRepository
import javax.inject.Inject

class RequestOnlyWaifuImUseCase @Inject constructor(private val repo: WaifusImRepository) {

    suspend operator fun invoke() = repo.requestOnlyWaifuIm()

    // suspend fun get() = repo.requestOnlyWaifuIm()

}

