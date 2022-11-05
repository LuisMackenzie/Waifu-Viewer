package com.mackenzie.waifuviewer.usecases.best

import com.mackenzie.waifuviewer.data.WaifusBestRepository
import com.mackenzie.waifuviewer.domain.WaifuBestItemPng
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FindWaifuPngUseCase @Inject constructor(private val repo: WaifusBestRepository) {

    operator fun invoke(id: Int): Flow<WaifuBestItemPng> = repo.findPngById(id)
}