package com.mackenzie.waifuviewer.usecases

import com.mackenzie.waifuviewer.data.WaifusImRepository

class GetOnlyWaifuImUseCase(private val repo: WaifusImRepository) {
    operator fun invoke() = repo.savedWaifusIm
}