package com.mackenzie.waifuviewer.models.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [WaifuPicItem::class, WaifuImItem::class], version = 1, exportSchema = false)
abstract class WaifuDataBase: RoomDatabase() {
    abstract fun waifuPicDao(): WaifuPicDao
    abstract fun waifuImDao(): WaifuImDao
}