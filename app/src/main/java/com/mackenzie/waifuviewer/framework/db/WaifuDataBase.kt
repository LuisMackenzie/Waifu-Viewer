package com.mackenzie.waifuviewer.framework.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [WaifuPicDbItem::class, WaifuImDbItem::class], version = 1, exportSchema = false)
abstract class WaifuDataBase: RoomDatabase() {
    abstract fun waifuPicDao(): WaifuPicDao
    abstract fun waifuImDao(): WaifuImDao
}