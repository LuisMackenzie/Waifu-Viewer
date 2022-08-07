package com.mackenzie.waifuviewer.usecases

import com.mackenzie.waifuviewer.data.WaifusPicRepository
import javax.inject.Inject

class GetOnlyWaifuPicUseCase @Inject constructor(private val repo: WaifusPicRepository) {
    operator fun invoke() = repo.savedWaifusPic
}