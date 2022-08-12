package com.mackenzie.waifuviewer.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WaifuImDao {

    @Query("SELECT * FROM WaifuImDbItem")
    fun getAllIm(): Flow<List<WaifuImDbItem>>

    @Query("SELECT * FROM WaifuImDbItem WHERE id = :id")
    fun findImById(id: Int): Flow<WaifuImDbItem>

    @Query("SELECT COUNT(id) FROM WaifuImDbItem")
    suspend fun waifuImCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWaifuIm(waifu: WaifuImDbItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllWaifuIm(waifus: List<WaifuImDbItem>)

    @Update
    fun updateWaifuIm(waifu: WaifuImDbItem)

    @Delete
    fun deleteIm(waifu: WaifuImDbItem)

    @Query("DELETE FROM WaifuImDbItem")
    suspend fun deleteAll()

}