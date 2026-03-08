package com.mackenzie.waifuviewer.data.server

import com.mackenzie.waifuviewer.data.server.models.WaifuImResult
import com.mackenzie.waifuviewer.data.server.models.WaifuImTagFullResult
import com.mackenzie.waifuviewer.data.server.models.WaifuImTagResult
import retrofit2.http.GET
import retrofit2.http.Query

interface WaifuImService {

    @GET("images/")
    suspend fun getRandomWaifuIm(
        @Query("IsNsfw") isNsfw:Boolean = false,
        @Query("IncludedTags") tags:String = "waifu",
        @Query("IsAnimated") isGif:Boolean = false,
        @Query("Orientation") orientation:String = "Portrait",
        @Query("PageSize") manyWaifus:Int = 30
    ): WaifuImResult

    @GET("images/")
    suspend fun getOnlyRandomWaifuIm(
        @Query("IsNsfw") isNsfw:Boolean = false,
        @Query("IncludedTags") tags:String = "waifu",
        @Query("IsAnimated") isGif:Boolean = false,
        @Query("Orientation") orientation:String = "Portrait"
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