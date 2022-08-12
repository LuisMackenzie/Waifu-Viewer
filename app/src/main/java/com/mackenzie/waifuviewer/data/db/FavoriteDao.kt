package com.mackenzie.waifuviewer.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM FavoriteDbItem")
    fun getAllFavs(): Flow<List<FavoriteDbItem>>

    @Query("SELECT * FROM FavoriteDbItem WHERE id = :id")
    fun findFavById(id: Int): Flow<FavoriteDbItem>

    @Query("SELECT COUNT(id) FROM FavoriteDbItem")
    suspend fun favoriteCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(waifu: FavoriteDbItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllFavorite(waifus: List<FavoriteDbItem>)

    @Update
    fun updateFav(waifu: FavoriteDbItem)

    @Delete
    fun deleteFav(waifu: FavoriteDbItem)

    @Query("DELETE FROM FavoriteDbItem")
    suspend fun deleteAll()

}