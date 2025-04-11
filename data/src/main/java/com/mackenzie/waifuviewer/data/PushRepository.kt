package com.mackenzie.waifuviewer.data

import com.mackenzie.waifuviewer.data.datasource.NotificationLocalDataSource
import com.mackenzie.waifuviewer.data.datasource.TokenLocalDataSource
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.FcmToken
import com.mackenzie.waifuviewer.domain.Notification
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PushRepository @Inject constructor(
    private val localTokenDataSource: TokenLocalDataSource,
    private val localPushDataSource: NotificationLocalDataSource
) {

    val tokenPush = localTokenDataSource.token

    val notifications = localPushDataSource.pushes

    fun findPushById(id: Int): Flow<Notification> = localPushDataSource.findPushById(id)

    fun findPushByPushId(pushId: String): Flow<Notification> = localPushDataSource.findPushByPushId(pushId)

    suspend fun saveToken(token: FcmToken): Error? {
        return localTokenDataSource.saveToken(token)
    }

    suspend fun saveAllTokens(tokens: List<FcmToken>): Error? {
        return localTokenDataSource.saveAllToken(tokens)
    }

    suspend fun savePush(push: Notification): Error? {
        return localPushDataSource.saveOnlyPush(push)
    }

    suspend fun saveAllPushes(pushes: List<Notification>): Error? {
        return localPushDataSource.savePushes(pushes)
    }

    suspend fun deleteNotification(push: Notification): Error? {
        return localPushDataSource.deletePush(push)
    }

    suspend fun deleteAllNotifications(): Error? {
        return localPushDataSource.deleteAll()
    }
}