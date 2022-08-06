package com.mackenzie.waifuviewer.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [WaifuImDbItem::class], version = 1, exportSchema = false)
abstract class WaifuImDataBase: RoomDatabase() {
    abstract fun waifuImDao(): WaifuImDao
}