package com.mackenzie.waifuviewer.data.server

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RemoteConnection {

    private val baseUrlWaifuIm = "https://api.waifu.im/"
    private val baseUrlWaifuPics = "https://api.waifu.pics/"
    // private val client = OkHttpClient.Builder().build()

    private val okHttpClient = HttpLoggingInterceptor().run {
        level = HttpLoggingInterceptor.Level.BODY
        OkHttpClient.Builder().addInterceptor(this).build()
    }


    private val builderIm = Retrofit.Builder()
        .baseUrl(baseUrlWaifuIm)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    private val builderPics = Retrofit.Builder()
        .baseUrl(baseUrlWaifuPics)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()


    val serviceIm = builderIm.create(WaifuImService::class.java)
    val servicePics = builderPics.create(WaifuImService::class.java)
}