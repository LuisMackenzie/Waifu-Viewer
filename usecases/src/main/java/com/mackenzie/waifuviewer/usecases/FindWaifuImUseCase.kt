package com.mackenzie.waifuviewer.usecases

import com.mackenzie.waifuviewer.data.WaifusImRepository
import com.mackenzie.waifuviewer.domain.WaifuImItem
import kotlinx.coroutines.flow.Flow

class FindWaifuImUseCase(private val repo: WaifusImRepository) {

    operator fun invoke(id: Int): Flow<WaifuImItem> = repo.findImById(id)

}