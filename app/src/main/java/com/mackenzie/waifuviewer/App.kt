package com.mackenzie.waifuviewer

import android.app.Application
import androidx.room.Room
import com.mackenzie.waifuviewer.models.db.WaifuDataBase

class App: Application() {

    lateinit var db: WaifuDataBase
        private set

    override fun onCreate() {
        super.onCreate()
        // instance = this
        db = Room.databaseBuilder(this, WaifuDataBase::class.java, "waifu_database").build()
    }

    /*companion object {
        lateinit var instance: App
    }*/
}