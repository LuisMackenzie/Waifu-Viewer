package com.mackenzie.waifuviewer.usecases.pics

import com.mackenzie.waifuviewer.data.WaifusPicRepository
import com.mackenzie.waifuviewer.domain.Error
import javax.inject.Inject

class ClearWaifuPicUseCase @Inject constructor(private val repo: WaifusPicRepository) {

    suspend operator fun invoke(): Error? = repo.requestClearWaifusPic()
}