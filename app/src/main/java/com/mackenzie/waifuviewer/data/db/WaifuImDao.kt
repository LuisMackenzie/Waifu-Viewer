package com.mackenzie.waifuviewer.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WaifuImDao {

    @Query("SELECT * FROM WaifuImItem")
    fun getAllIm(): Flow<List<WaifuImItem>>

    @Query("SELECT * FROM WaifuImItem WHERE id = :id")
    fun findImById(id: Int): Flow<WaifuImItem>

    @Query("SELECT COUNT(id) FROM WaifuImItem")
    fun waifuImCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWaifuIm(waifu: WaifuImItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllWaifuIm(waifus: List<WaifuImItem>)

    @Update
    fun updateWaifuIm(waifu: WaifuImItem)

}