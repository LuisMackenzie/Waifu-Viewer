package com.mackenzie.waifuviewer.usecases.push

import com.mackenzie.waifuviewer.data.PushRepository
import com.mackenzie.waifuviewer.domain.Notification
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FindPushUseCase @Inject constructor(private val repo: PushRepository)  {

    operator fun invoke(id: Int): Flow<Notification> = repo.findPushById(id)
}