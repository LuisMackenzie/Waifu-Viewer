package com.mackenzie.waifuviewer.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mackenzie.waifuviewer.data.db.dao.FavoriteDao
import com.mackenzie.waifuviewer.data.db.dao.WaifuBestDao
import com.mackenzie.waifuviewer.data.db.dao.WaifuFcmTokenDao
import com.mackenzie.waifuviewer.data.db.dao.WaifuImDao
import com.mackenzie.waifuviewer.data.db.dao.WaifuImTagsDao
import com.mackenzie.waifuviewer.data.db.dao.WaifuPicDao
import com.mackenzie.waifuviewer.data.db.dao.WaifuPushDao

@Database(entities = [
    WaifuPicDbItem::class,
    WaifuImDbItem::class,
    WaifuBestDbItem::class,
    WaifuImTagDb::class,
    FavoriteDbItem::class,
    FcmTokenDb::class,
    NotificationDb::class], version = 1, exportSchema = false)
abstract class WaifuDataBase: RoomDatabase() {
    abstract fun waifuPicDao(): WaifuPicDao
    abstract fun waifuImDao(): WaifuImDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun waifuBestDao(): WaifuBestDao
    abstract fun waifuImTagsDao(): WaifuImTagsDao
    abstract fun waifuFcmTokenDao(): WaifuFcmTokenDao
    abstract fun waifuPushDao(): WaifuPushDao

    companion object {
        const val DATABASE_NAME = "waifu-database"

        @Volatile
        private var INSTANCE: WaifuDataBase? = null

        fun getDatabase(context: Context): WaifuDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WaifuDataBase::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
