package com.mackenzie.waifuviewer.data.server

import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.*

interface WaifuImService {

    @GET("random/")
    suspend fun getRandomWaifuIm(
        @Query("is_nsfw") isNsfw:Boolean = false,
        @Query("selected_tags") tags:String = "waifu",
        @Query("gif") isGif:Boolean = false,
        @Query("orientation") orientation:String = "PORTRAIT",
        @Query("many") manyWaifus:Boolean = true
    ): WaifuResult

    /*@GET("info/")
    suspend fun getOnlyWaifuIm(@Query("images") nameId:String): WaifuResult*/

    @GET("random/")
    suspend fun getOnlyRandomWaifuIm(
        @Query("is_nsfw") isNsfw:Boolean = false,
        @Query("selected_tags") tags:String = "waifu",
        @Query("gif") isGif:Boolean = false,
        @Query("orientation") orientation:String = "PORTRAIT",
        @Query("many") manyWaifus:Boolean = false
    ): WaifuResult

   /* @GET("random/")
    suspend fun getEspecialWaifu(
        @Query("is_nsfw") isNsfw:Boolean = false,
        @Query("selected_tags") tags:String,
        @Query("gif") isGif:Boolean = false,
        @Query("many") manyWaifus:Boolean = true
    ): WaifuResult*/



}