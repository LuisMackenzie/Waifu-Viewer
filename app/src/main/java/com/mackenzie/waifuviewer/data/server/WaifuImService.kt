package com.mackenzie.waifuviewer.data.server

import retrofit2.http.GET
import retrofit2.http.Query

interface WaifuImService {

    @GET("search/")
    suspend fun getRandomWaifuIm(
        @Query("is_nsfw") isNsfw:Boolean = false,
        @Query("included_tags") tags:String = "waifu",
        @Query("gif") isGif:Boolean = false,
        @Query("orientation") orientation:String = "PORTRAIT",
        @Query("limit") manyWaifus:Int = 30
    ): WaifuImResult

    @GET("search/")
    suspend fun getOnlyRandomWaifuIm(
        @Query("is_nsfw") isNsfw:Boolean = false,
        @Query("included_tags") tags:String = "waifu",
        @Query("gif") isGif:Boolean = false,
        @Query("orientation") orientation:String = "PORTRAIT"
    ): WaifuImResult
}