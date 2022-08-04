package com.mackenzie.waifuviewer.usecases

import com.mackenzie.waifuviewer.data.WaifusImRepository
import com.mackenzie.waifuviewer.domain.WaifuImItem
import kotlinx.coroutines.flow.Flow

class GetWaifuImUseCase(private val repo: WaifusImRepository) {

    operator fun invoke(): Flow<List<WaifuImItem>> = repo.savedWaifusIm

}