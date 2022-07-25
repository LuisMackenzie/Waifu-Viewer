package com.mackenzie.waifuviewer.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WaifuPicDao {

    @Query("SELECT * FROM WaifuPicItem")
    fun getAllPic(): Flow<List<WaifuPicItem>>

    @Query("SELECT * FROM WaifuPicItem WHERE id = :id")
    fun findPicsById(id: Int): Flow<WaifuPicItem>

    @Query("SELECT COUNT(id) FROM WaifuPicItem")
    fun waifuPicsCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWaifuPics(waifu: WaifuPicItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllWaifuPics(waifus: List<WaifuPicItem>)

    @Update
    fun updateWaifuPics(waifu: WaifuPicItem)

    /*@Delete
    fun delete(waifu: WaifuPicItem)*/

}