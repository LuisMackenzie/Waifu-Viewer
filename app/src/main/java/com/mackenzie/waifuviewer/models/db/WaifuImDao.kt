package com.mackenzie.waifuviewer.models.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WaifuImDao {

    @Query("SELECT * FROM WaifuImItem")
    fun getAllIm(): Flow<List<WaifuImItem>>

    @Query("SELECT * FROM WaifuImItem WHERE id = :id")
    fun findById(id: Int): Flow<WaifuImItem>

    @Query("SELECT COUNT(id) FROM WaifuImItem")
    suspend fun waifuCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWaifu(waifu: WaifuImItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllWaifus(waifus: List<WaifuImItem>)

    @Update
    suspend fun updateWaifu(waifu: WaifuImItem)

}