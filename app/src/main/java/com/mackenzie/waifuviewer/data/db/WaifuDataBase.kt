package com.mackenzie.waifuviewer.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [WaifuPicDbItem::class, WaifuImDbItem::class, WaifuBestDbItem::class, FavoriteDbItem::class], version = 1, exportSchema = false)
abstract class WaifuDataBase: RoomDatabase() {
    abstract fun waifuPicDao(): WaifuPicDao
    abstract fun waifuImDao(): WaifuImDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun waifuBestDao(): WaifuBestDao
}