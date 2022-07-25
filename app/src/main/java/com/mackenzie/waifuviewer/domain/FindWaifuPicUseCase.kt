package com.mackenzie.waifuviewer.domain

import com.mackenzie.waifuviewer.data.datasource.WaifusPicRepository
import com.mackenzie.waifuviewer.data.db.WaifuPicItem
import kotlinx.coroutines.flow.Flow

class FindWaifuPicUseCase(private val repo: WaifusPicRepository) {

    operator fun invoke(id: Int) : Flow<WaifuPicItem> = repo.findPicsById(id)
}