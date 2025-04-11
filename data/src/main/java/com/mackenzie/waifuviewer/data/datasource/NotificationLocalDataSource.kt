package com.mackenzie.waifuviewer.data.datasource

import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.Notification
import kotlinx.coroutines.flow.Flow

interface NotificationLocalDataSource {
    val pushes: Flow<List<Notification>>
    suspend fun isEmpty(): Boolean
    fun findPushById(id: Int): Flow<Notification>
    fun findPushByPushId(pushId: String): Flow<Notification>
    suspend fun savePushes(pushes: List<Notification>): Error?
    suspend fun saveOnlyPush(push: Notification): Error?
    suspend fun deletePush(push: Notification): Error?
    suspend fun deleteAll(): Error?
}