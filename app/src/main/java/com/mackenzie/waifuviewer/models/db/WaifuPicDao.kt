package com.mackenzie.waifuviewer.models.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WaifuPicDao {

    @Query("SELECT * FROM WaifuPicItem")
    fun getAllPic(): Flow<List<WaifuPicItem>>

    @Query("SELECT * FROM WaifuPicItem WHERE id = :id")
    fun findById(id: Int): Flow<WaifuPicItem>

    @Query("SELECT COUNT(id) FROM WaifuPicItem")
    fun waifuCount(): Int

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    fun insertWaifu(waifu: WaifuPicItem)

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    fun insertAllWaifus(waifus: List<WaifuPicItem>)

    @Update
    fun updateWaifu(waifu: WaifuPicItem)

    /*@Delete
    fun delete(waifu: WaifuPicItem)*/

}