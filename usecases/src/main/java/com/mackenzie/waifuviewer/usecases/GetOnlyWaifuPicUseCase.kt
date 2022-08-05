package com.mackenzie.waifuviewer.usecases

import com.mackenzie.waifuviewer.data.WaifusPicRepository
import com.mackenzie.waifuviewer.domain.WaifuPicItem
import kotlinx.coroutines.flow.Flow

class GetOnlyWaifuPicUseCase(private val repo: WaifusPicRepository) {

    operator fun invoke() = repo.savedWaifusPic

}