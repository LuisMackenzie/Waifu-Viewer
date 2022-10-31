package com.mackenzie.waifuviewer.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WaifuBestDao {

    @Query("SELECT * FROM WaifuBestPngDbItem")
    fun getAllPng(): Flow<List<WaifuBestPngDbItem>>

    @Query("SELECT * FROM WaifuBestPngDbItem WHERE id = :id")
    fun findPngById(id: Int): Flow<WaifuBestPngDbItem>

    @Query("SELECT COUNT(id) FROM WaifuBestPngDbItem")
    suspend fun waifuPngCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWaifuPng(waifu: WaifuBestPngDbItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllWaifuPng(waifus: List<WaifuBestPngDbItem>)

    @Update
    suspend fun updateWaifuPng(waifu: WaifuBestPngDbItem)

    @Delete
    suspend fun deletePng(waifu: WaifuBestPngDbItem)

    @Query("DELETE FROM WaifuBestPngDbItem")
    suspend fun deleteAllPng()


    @Query("SELECT * FROM WaifuBestGifDbItem")
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
    suspend fun deleteAllGif()
}