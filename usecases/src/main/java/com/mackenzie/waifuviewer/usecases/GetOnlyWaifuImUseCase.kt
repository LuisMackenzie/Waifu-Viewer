package com.mackenzie.waifuviewer.usecases

import com.mackenzie.waifuviewer.data.WaifusImRepository
import com.mackenzie.waifuviewer.domain.WaifuImItem
import kotlinx.coroutines.flow.Flow

class GetOnlyWaifuImUseCase(private val repo: WaifusImRepository) {

    operator fun invoke() = repo.savedWaifusIm
}