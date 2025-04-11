package com.mackenzie.waifuviewer.usecases.push

import com.mackenzie.waifuviewer.data.PushRepository
import com.mackenzie.waifuviewer.domain.Notification
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FindPushByPushIdUseCase @Inject constructor(private val repo: PushRepository)  {

    operator fun invoke(pushId: String): Flow<Notification> = repo.findPushByPushId(pushId)
}