package com.mackenzie.waifuviewer.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [WaifuImDbItem::class, WaifuPicDbItem::class], version = 1, exportSchema = false)
abstract class WaifuDataBase: RoomDatabase() {
    abstract fun waifuImDao(): WaifuImDao
    abstract fun waifuPicDao(): WaifuPicDao
}