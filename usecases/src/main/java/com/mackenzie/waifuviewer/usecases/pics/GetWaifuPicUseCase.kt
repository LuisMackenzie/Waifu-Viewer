package com.mackenzie.waifuviewer.usecases.pics

import com.mackenzie.waifuviewer.data.WaifusPicRepository
import javax.inject.Inject

class GetWaifuPicUseCase @Inject constructor(private val repo: WaifusPicRepository) {
    operator fun invoke() = repo.savedWaifusPic
}