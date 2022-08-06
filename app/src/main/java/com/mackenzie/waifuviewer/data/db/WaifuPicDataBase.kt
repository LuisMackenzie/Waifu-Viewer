package com.mackenzie.waifuviewer.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [WaifuPicDbItem::class], version = 1, exportSchema = false)
abstract class WaifuPicDataBase: RoomDatabase() {
    abstract fun waifuPicDao(): WaifuPicDao
}