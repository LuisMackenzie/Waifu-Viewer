package com.mackenzie.waifuviewer.usecases.push

import com.mackenzie.waifuviewer.data.PushRepository
import com.mackenzie.waifuviewer.domain.Error
import javax.inject.Inject

class ClearAllNotificationsUseCase @Inject constructor(private val repo: PushRepository) {

    suspend operator fun invoke(): Error? = repo.deleteAllNotifications()
}