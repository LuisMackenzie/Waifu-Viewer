package com.mackenzie.waifuviewer.usecases

import com.mackenzie.waifuviewer.data.WaifusPicRepository

class GetOnlyWaifuPicUseCase(private val repo: WaifusPicRepository) {
    operator fun invoke() = repo.savedWaifusPic
}