package com.mackenzie.waifuviewer.usecases.best

import com.mackenzie.waifuviewer.data.WaifusBestRepository
import javax.inject.Inject

class GetWaifuBestUseCase @Inject constructor(private val repo: WaifusBestRepository) {
    operator fun invoke() = repo.savedWaifus
}