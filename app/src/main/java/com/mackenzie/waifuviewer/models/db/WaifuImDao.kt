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
    fun waifuCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWaifu(waifu: WaifuImItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllWaifus(waifus: List<WaifuImItem>)

    @Update
    fun updateWaifu(waifu: WaifuImItem)

}