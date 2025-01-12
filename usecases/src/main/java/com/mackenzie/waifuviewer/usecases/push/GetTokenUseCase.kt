package com.mackenzie.waifuviewer.usecases.push

import com.mackenzie.waifuviewer.data.PushRepository
import javax.inject.Inject

class GetTokenUseCase @Inject constructor(private val repo: PushRepository) {

    operator fun invoke() = repo.tokenPush
}