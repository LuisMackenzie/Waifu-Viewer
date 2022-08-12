package com.mackenzie.waifuviewer.usecases

import com.mackenzie.waifuviewer.data.WaifusImRepository
import com.mackenzie.waifuviewer.domain.Error
import javax.inject.Inject

class ClearWaifuImUseCase @Inject constructor(private val repo: WaifusImRepository) {

    suspend operator fun invoke(): Error? = repo.requestClearWaifusIm()
}