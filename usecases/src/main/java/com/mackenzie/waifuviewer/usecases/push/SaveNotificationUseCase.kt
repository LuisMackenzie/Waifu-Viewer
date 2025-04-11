package com.mackenzie.waifuviewer.usecases.push

import com.mackenzie.waifuviewer.data.PushRepository
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.Notification
import javax.inject.Inject

class SaveNotificationUseCase @Inject constructor(private val repo: PushRepository) {

    suspend operator fun invoke(push: Notification): Error? = repo.savePush(push)
}