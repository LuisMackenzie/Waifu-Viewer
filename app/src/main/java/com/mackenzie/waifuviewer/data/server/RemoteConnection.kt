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

    fun <T> buildServiceIm(service: Class<T>): T {
        return builderIm.create(service)
    }

    fun <T> buildServicePics(service: Class<T>): T {
        return builderPics.create(service)
    }

    val serviceIm = builderIm.create(WaifuService::class.java)
    val servicePics = builderPics.create(WaifuService::class.java)
    // val servicePics = buildServicePics(WaifuManager::class.java)
    // val serviceIm = buildServiceIm(WaifuService::class.java)
    // val servicePics = builderPics.create(WaifuService::class.java)
}