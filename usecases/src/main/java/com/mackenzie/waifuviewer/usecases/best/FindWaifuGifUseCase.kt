package com.mackenzie.waifuviewer.usecases.best

import com.mackenzie.waifuviewer.data.WaifusBestRepository
import com.mackenzie.waifuviewer.domain.WaifuBestItemGif
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FindWaifuGifUseCase @Inject constructor(private val repo: WaifusBestRepository) {

    operator fun invoke(id: Int): Flow<WaifuBestItemGif> = repo.findGifById(id)
}