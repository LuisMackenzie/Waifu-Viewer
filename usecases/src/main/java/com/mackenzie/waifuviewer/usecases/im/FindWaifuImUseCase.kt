package com.mackenzie.waifuviewer.usecases.im

import com.mackenzie.waifuviewer.data.WaifusImRepository
import com.mackenzie.waifuviewer.domain.im.WaifuImItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FindWaifuImUseCase @Inject constructor(private val repo: WaifusImRepository) {

    operator fun invoke(id: Int): Flow<WaifuImItem> = repo.findImById(id)

}