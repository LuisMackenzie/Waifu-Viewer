package com.mackenzie.waifuviewer.data

import com.mackenzie.waifuviewer.data.datasource.NotificationLocalDataSource
import com.mackenzie.waifuviewer.data.datasource.TokenLocalDataSource
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.FavoriteItem
import com.mackenzie.waifuviewer.domain.FcmToken
import com.mackenzie.waifuviewer.domain.Notification
import com.mackenzie.waifuviewer.domain.im.WaifuImItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PushRepository @Inject constructor(
    private val tokenDataSource: TokenLocalDataSource,
    private val pushDataSource: NotificationLocalDataSource
) {

    val tokenPush = tokenDataSource.token

    val notifications = pushDataSource.pushes

    fun findPushById(id: Int): Flow<Notification> = pushDataSource.findPushById(id)

    fun findPushByPushId(pushId: String): Flow<Notification> = pushDataSource.findPushByPushId(pushId)

    suspend fun saveToken(token: FcmToken): Error? {
        return tokenDataSource.saveToken(token)
    }

    suspend fun saveAllTokens(tokens: List<FcmToken>): Error? {
        return tokenDataSource.saveAllToken(tokens)
    }

    suspend fun savePush(push: Notification): Error? {
        return pushDataSource.saveOnlyPush(push)
    }

    suspend fun saveAllPushes(pushes: List<Notification>): Error? {
        return pushDataSource.savePushes(pushes)
    }

    suspend fun deleteNotification(push: Notification): Error? {
        return pushDataSource.deletePush(push)
    }

    suspend fun deleteAllNotifications(): Error? {
        return pushDataSource.deleteAll()
    }
}