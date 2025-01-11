package com.mackenzie.waifuviewer.data.db.datasources


import com.mackenzie.waifuviewer.data.datasource.NotificationLocalDataSource
import com.mackenzie.waifuviewer.data.db.NotificationDb
import com.mackenzie.waifuviewer.data.db.dao.WaifuPushDao
import com.mackenzie.waifuviewer.data.tryCall
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.Notification
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoomNotificationDataSource @Inject constructor(private val dao: WaifuPushDao): NotificationLocalDataSource {

    override val pushes: Flow<List<Notification>> = dao.getAllPush().map { it.toDomainModel() }

    override suspend fun isEmpty(): Boolean = dao.pushCount() == 0

    override fun findPushById(id: Int): Flow<Notification> = dao.findPushById(id).map { it.toDomainModel() }

    override fun findPushByPushId(pushId: String): Flow<Notification> = dao.findPushByPushId(pushId).map { it.toDomainModel() }

    override suspend fun savePushes(pushes: List<Notification>): Error? = tryCall {
        dao.insertAllPush(pushes.fromDomainModel())
    }.fold(ifLeft = { it }, ifRight = { null })

    override suspend fun saveOnlyPush(push: Notification): Error? = tryCall {
        dao.insertPush(push.fromDomainModel())
    }.fold(ifLeft = { it }, ifRight = { null })

    override suspend fun deletePush(push: Notification): Error? = tryCall {
        dao.deletePush(push.fromDomainModel())
    }.fold(ifLeft = { it }, ifRight = { null })

    override suspend fun deleteAll(): Error? = tryCall {
        dao.deleteAllPush()
    }.fold(ifLeft = { it }, ifRight = { null })

}

private fun List<NotificationDb>.toDomainModel(): List<Notification> = map { it.toDomainModel() }

private fun NotificationDb.toDomainModel(): Notification =
    Notification(
        id,
        pushId,
        date,
        title,
        description,
        isRead,
        type
    )

private fun List<Notification>.fromDomainModel(): List<NotificationDb> = map { it.fromDomainModel() }

private fun Notification.fromDomainModel(): NotificationDb =
    NotificationDb(
        id,
        pushId,
        date,
        title,
        description,
        isRead,
        type
    )