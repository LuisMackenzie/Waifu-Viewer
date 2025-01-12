package com.mackenzie.waifuviewer.usecases.push

import com.mackenzie.waifuviewer.data.PushRepository
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.FcmToken
import javax.inject.Inject

class SaveTokenUseCase @Inject constructor(private val repo: PushRepository) {

    suspend operator fun invoke(token: FcmToken): Error? = repo.saveToken(token)
}