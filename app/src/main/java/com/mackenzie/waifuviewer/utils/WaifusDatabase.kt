package com.mackenzie.waifuviewer.utils

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class WaifusDatabase(private val context: Context) {
    companion object {
        @SuppressLint("StaticFieldLeak")
        private var waifuDB: WaifusDatabase? = null

        fun getInstance(context: Context): WaifusDatabase {
            if (waifuDB == null) {
                waifuDB = WaifusDatabase(context.applicationContext)
            }
            return waifuDB!!
        }

    }
}