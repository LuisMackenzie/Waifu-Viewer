package com.mackenzie.waifuviewer.usecases.best

import com.mackenzie.waifuviewer.data.WaifusBestRepository
import com.mackenzie.waifuviewer.domain.WaifuBestItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FindWaifuBestUseCase @Inject constructor(private val repo: WaifusBestRepository) {

    operator fun invoke(id: Int): Flow<WaifuBestItem> = repo.findById(id)
}