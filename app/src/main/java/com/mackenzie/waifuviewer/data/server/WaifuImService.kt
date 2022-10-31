package com.mackenzie.waifuviewer.data.server

import retrofit2.http.*

interface WaifuImService {

    @GET("random/")
    suspend fun getRandomWaifuIm(
        @Query("is_nsfw") isNsfw:Boolean = false,
        @Query("selected_tags") tags:String = "waifu",
        @Query("gif") isGif:Boolean = false,
        @Query("orientation") orientation:String = "PORTRAIT",
        @Query("many") manyWaifus:Boolean = true
    ): WaifuImResult

    @GET("random/")
    suspend fun getOnlyRandomWaifuIm(
        @Query("is_nsfw") isNsfw:Boolean = false,
        @Query("selected_tags") tags:String = "waifu",
        @Query("gif") isGif:Boolean = false,
        @Query("orientation") orientation:String = "PORTRAIT",
        @Query("many") manyWaifus:Boolean = false
    ): WaifuImResult
}