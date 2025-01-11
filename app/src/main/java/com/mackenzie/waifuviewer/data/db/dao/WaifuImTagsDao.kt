package com.mackenzie.waifuviewer.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mackenzie.waifuviewer.data.db.WaifuImTagDb
import kotlinx.coroutines.flow.Flow

@Dao
interface WaifuImTagsDao {

    @Query("SELECT * FROM WaifuImTagDb")
    fun getAllImTags(): Flow<WaifuImTagDb>

    @Query("SELECT COUNT(id) FROM WaifuImTagDb")
    suspend fun waifuImTagCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllImTags(waifuImTags: WaifuImTagDb)

}