package com.mackenzie.waifuviewer.data.server

import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface WaifuPicService {

    @POST("{many}/{type}/{category}")
    suspend fun getRandomWaifuPics(
        @Path("many") many:String = "many",
        @Path("type") type:String = "sfw",
        @Path("category") category:String = "waifu",
        @Body() body: WaifuPicsRequest
    ): Response<WaifuPicsResult>

    @GET("{type}/{category}")
    suspend fun getOnlyWaifuPic(
        @Path("type") type:String = "sfw",
        @Path("category") category:String = "waifu"
    ): WaifuPic

}