package com.mackenzie.waifuviewer.usecases

import com.mackenzie.waifuviewer.data.WaifusImRepository
import javax.inject.Inject

class GetOnlyWaifuImUseCase @Inject constructor(private val repo: WaifusImRepository) {
    operator fun invoke() = repo.savedWaifusIm
}