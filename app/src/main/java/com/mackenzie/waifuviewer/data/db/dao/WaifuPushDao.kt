package com.mackenzie.waifuviewer.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mackenzie.waifuviewer.data.db.NotificationDb
import kotlinx.coroutines.flow.Flow

@Dao
interface WaifuPushDao {

    @Query("SELECT * FROM NotificationDb")
    fun getAllPush(): Flow<List<NotificationDb>>

    @Query("SELECT * FROM NotificationDb WHERE id = :id")
    fun findPushById(id: Int): Flow<NotificationDb>

    @Query("SELECT * FROM NotificationDb WHERE id = :pushId")
    fun findPushByPushId(pushId: String): Flow<NotificationDb>

    @Query("SELECT COUNT(id) FROM NotificationDb")
    suspend fun pushCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPush(push: NotificationDb)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPush(pushes: List<NotificationDb>)

    @Delete
    suspend fun deletePush(push: NotificationDb)

    @Query("DELETE FROM NotificationDb")
    suspend fun deleteAllPush()
}