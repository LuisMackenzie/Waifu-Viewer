package com.mackenzie.waifuviewer.data.server

import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.*

interface WaifuService {

    @GET("random/")
    suspend fun getRandomWaifuIm(
        @Query("is_nsfw") isNsfw:Boolean = false,
        @Query("selected_tags") tags:String = "waifu",
        @Query("gif") isGif:Boolean = false,
        @Query("orientation") orientation:String = "PORTRAIT",
        @Query("many") manyWaifus:Boolean = true
    ): WaifuResult

    @GET("info/")
    suspend fun getOnlyWaifuIm(@Query("images") nameId:String): WaifuResult

    @GET("endpoints/")
    suspend fun getCategories(@Query("full") fullInfo:Boolean = true): TagResult

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

    @GET("{type}/{category}")
    suspend fun getOnlyWaifuPic(
        @Path("type") type:String = "sfw",
        @Path("category") category:String = "waifu"
    ): WaifuPic

    @POST("{many}/{type}/{category}")
    suspend fun getRandomWaifuPics(
        @Path("many") many:String = "many",
        @Path("type") type:String = "sfw",
        @Path("category") category:String = "waifu",
        @Body() body: JsonObject
    ): Response<WaifuPicsResult>

}