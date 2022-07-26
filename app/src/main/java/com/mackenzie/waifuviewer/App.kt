package com.mackenzie.waifuviewer

import android.app.Application
import androidx.room.Room
import com.mackenzie.waifuviewer.framework.db.WaifuDataBase

class App: Application() {

    lateinit var db: WaifuDataBase
        private set

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(this, WaifuDataBase::class.java, "waifu-database").build()
    }
}