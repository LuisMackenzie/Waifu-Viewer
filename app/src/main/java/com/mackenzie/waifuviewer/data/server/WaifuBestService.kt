package com.mackenzie.waifuviewer.data.server

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface  WaifuBestService {

    @GET("{tags}")
    suspend fun getRandomWaifuBestPng(
        @Path("tags") tags:String,
        @Query("amount") amount:Int = 20,
    ): WaifuBestPngResult

    @GET("{tags}")
    suspend fun getRandomWaifuBestGif(
        @Path("tags") tags:String,
        @Query("amount") amount:Int = 20,
    ): WaifuBestGifResult

    @GET("{tags}")
    suspend fun getOnlyRandomWaifuBestPng(
        @Path("tags") tags:String = "neko",
        @Query("amount") amount:Int = 1,
    ): WaifuBestPngResult

    @GET("{tags}")
    suspend fun getOnlyRandomWaifuBestGif(
        @Path("tags") tags:String = "neko",
        @Query("amount") amount:Int = 1,
    ): WaifuBestGifResult

    /*@GET("endpoints")
    suspend fun getCategoriesWaifuBest(): CategoriesResult*/

}

