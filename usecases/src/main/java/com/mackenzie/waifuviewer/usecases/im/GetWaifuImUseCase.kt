package com.mackenzie.waifuviewer.usecases.im

import com.mackenzie.waifuviewer.data.WaifusImRepository
import javax.inject.Inject

class GetWaifuImUseCase @Inject constructor(private val repo: WaifusImRepository) {
    operator fun invoke() = repo.savedWaifusIm
}