package com.mackenzie.waifuviewer.domain

import com.mackenzie.waifuviewer.data.datasource.WaifusImRepository
import com.mackenzie.waifuviewer.data.db.WaifuImItem
import kotlinx.coroutines.flow.Flow

class FindWaifuImUseCase(private val repo: WaifusImRepository) {

    operator fun invoke(id: Int): Flow<WaifuImItem> = repo.findImById(id)

}