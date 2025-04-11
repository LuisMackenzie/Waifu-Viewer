package com.mackenzie.waifuviewer.data.db.dao

import androidx.room.*
import com.mackenzie.waifuviewer.data.db.WaifuPicDbItem
import kotlinx.coroutines.flow.Flow

@Dao
interface WaifuPicDao {

    @Query("SELECT * FROM WaifuPicDbItem")
    fun getAllPic(): Flow<List<WaifuPicDbItem>>

    @Query("SELECT * FROM WaifuPicDbItem WHERE id = :id")
    fun findPicsById(id: Int): Flow<WaifuPicDbItem>

    @Query("SELECT COUNT(id) FROM WaifuPicDbItem")
    suspend fun waifuPicsCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWaifuPics(waifu: WaifuPicDbItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllWaifuPics(waifus: List<WaifuPicDbItem>)

    @Update
    suspend fun updateWaifuPics(waifu: WaifuPicDbItem)

    @Delete
    suspend fun deletePics(waifu: WaifuPicDbItem)

    @Query("DELETE FROM WaifuPicDbItem")
    suspend fun deleteAll()

}