package com.mackenzie.waifuviewer

import android.app.Application
import androidx.room.Room
import com.mackenzie.waifuviewer.data.db.WaifuImDataBase
import com.mackenzie.waifuviewer.data.db.WaifuPicDataBase

class App: Application() {

    lateinit var imDataBase: WaifuImDataBase
        private set

    lateinit var picDataBase: WaifuPicDataBase
        private set

    override fun onCreate() {
        super.onCreate()
        imDataBase = Room.databaseBuilder(this, WaifuImDataBase::class.java, "waifu-im-database").build()
        picDataBase = Room.databaseBuilder(this, WaifuPicDataBase::class.java, "waifu-pic-database").build()
    }
}