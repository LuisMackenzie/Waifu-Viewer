package com.mackenzie.waifuviewer.usecases

import com.mackenzie.waifuviewer.data.WaifusPicRepository
import com.mackenzie.waifuviewer.domain.WaifuPicItem
import kotlinx.coroutines.flow.Flow

class GetWaifuPicUseCase(private val repo: WaifusPicRepository) {

    operator fun invoke(): Flow<List<WaifuPicItem>> = repo.savedWaifusPic

}