package com.mackenzie.waifuviewer.usecases.im

import com.mackenzie.waifuviewer.data.WaifusImRepository
import javax.inject.Inject

class RequestOnlyWaifuImUseCase @Inject constructor(private val repo: WaifusImRepository) {

    suspend operator fun invoke(orientation: Boolean) = repo.requestOnlyWaifuIm(orientation)

}

