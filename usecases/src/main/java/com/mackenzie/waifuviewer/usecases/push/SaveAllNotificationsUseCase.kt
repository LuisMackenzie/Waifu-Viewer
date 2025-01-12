package com.mackenzie.waifuviewer.usecases.push

import com.mackenzie.waifuviewer.data.PushRepository
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.Notification
import javax.inject.Inject

class SaveAllNotificationsUseCase @Inject constructor(private val repo: PushRepository) {

    suspend operator fun invoke(pushes: List<Notification>): Error? = repo.saveAllPushes(pushes)
}