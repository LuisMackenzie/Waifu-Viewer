package com.mackenzie.waifuviewer.data.server

import com.mackenzie.waifuviewer.data.server.models.WaifuImResult
import com.mackenzie.waifuviewer.data.server.models.WaifuImTagFullResult
import com.mackenzie.waifuviewer.data.server.models.WaifuImTagResult
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

    @GET("tags/")
    suspend fun getTagsWaifuIm(
        @Query("full") full:Boolean = false,
    ): WaifuImTagResult

    @GET("tags/")
    suspend fun getTagsWaifuImFull(
        @Query("full") full:Boolean = true,
    ): WaifuImTagFullResult
}