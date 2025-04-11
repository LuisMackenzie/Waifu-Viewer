package com.mackenzie.waifuviewer.data.db.dao

import androidx.room.*
import com.mackenzie.waifuviewer.data.db.WaifuBestDbItem
import kotlinx.coroutines.flow.Flow

@Dao
interface WaifuBestDao {

    @Query("SELECT * FROM WaifuBestDbItem")
    fun getAll(): Flow<List<WaifuBestDbItem>>

    @Query("SELECT * FROM WaifuBestDbItem WHERE id = :id")
    fun findById(id: Int): Flow<WaifuBestDbItem>

    @Query("SELECT COUNT(id) FROM WaifuBestDbItem")
    suspend fun waifuCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWaifu(waifu: WaifuBestDbItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllWaifu(waifus: List<WaifuBestDbItem>)

    @Update
    suspend fun updateWaifu(waifu: WaifuBestDbItem)

    @Delete
    suspend fun delete(waifu: WaifuBestDbItem)

    @Query("DELETE FROM WaifuBestDbItem")
    suspend fun deleteAll()


    /*@Query("SELECT * FROM WaifuBestGifDbItem")
    fun getAllGif(): Flow<List<WaifuBestGifDbItem>>

    @Query("SELECT * FROM WaifuBestGifDbItem WHERE id = :id")
    fun findGifById(id: Int): Flow<WaifuBestGifDbItem>

    @Query("SELECT COUNT(id) FROM WaifuBestGifDbItem")
    suspend fun waifuGifCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWaifuGif(waifu: WaifuBestGifDbItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllWaifuGif(waifus: List<WaifuBestGifDbItem>)

    @Update
    suspend fun updateWaifuGif(waifu: WaifuBestGifDbItem)

    @Delete
    suspend fun deleteGif(waifu: WaifuBestGifDbItem)

    @Query("DELETE FROM WaifuBestGifDbItem")
    suspend fun deleteAllGif()*/
}