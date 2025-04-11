package com.mackenzie.waifuviewer.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mackenzie.waifuviewer.data.db.FcmTokenDb
import kotlinx.coroutines.flow.Flow

@Dao
interface WaifuFcmTokenDao {

    @Query("SELECT * FROM FcmTokenDb")
    fun getFcmToken(): Flow<FcmTokenDb>

    @Query("SELECT COUNT(id) FROM FcmTokenDb")
    suspend fun tokenCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToken(token: FcmTokenDb)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTokens(token: List<FcmTokenDb>)

}