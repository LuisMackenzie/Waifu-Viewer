package com.mackenzie.waifuviewer.data.server

import retrofit2.http.GET
import retrofit2.http.Query

interface WaifuTraceMoeService {

    @GET("search")
    suspend fun searchAnime(
        @Query("url") imageUrl:String
    ): TraceMoeResult

}