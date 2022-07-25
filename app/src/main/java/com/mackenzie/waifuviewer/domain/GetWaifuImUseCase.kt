package com.mackenzie.waifuviewer.domain

import com.mackenzie.waifuviewer.data.datasource.WaifusImRepository
import com.mackenzie.waifuviewer.data.db.WaifuImItem
import kotlinx.coroutines.flow.Flow

class GetWaifuImUseCase(private val repo: WaifusImRepository) {

    operator fun invoke(): Flow<List<WaifuImItem>> = repo.savedWaifusIm

}