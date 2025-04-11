package com.mackenzie.waifuviewer.usecases.push

import com.mackenzie.waifuviewer.data.PushRepository
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.FcmToken
import javax.inject.Inject

class SaveAllTokensUseCase @Inject constructor(private val repo: PushRepository) {

    suspend operator fun invoke(tokens: List<FcmToken>): Error? = repo.saveAllTokens(tokens)
}