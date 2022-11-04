package com.mackenzie.waifuviewer.usecases.pics

import com.mackenzie.waifuviewer.data.WaifusPicRepository
import com.mackenzie.waifuviewer.domain.WaifuPicItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FindWaifuPicUseCase @Inject constructor(private val repo: WaifusPicRepository) {

    operator fun invoke(id: Int) : Flow<WaifuPicItem> = repo.findPicsById(id)
}