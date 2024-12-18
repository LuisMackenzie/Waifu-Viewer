package com.mackenzie.waifuviewer.data.server

import com.mackenzie.waifuviewer.data.server.models.TraceMoeResult
import retrofit2.http.GET
import retrofit2.http.Query

interface WaifuTraceMoeService {

    @GET("search")
    suspend fun searchAnime(
        @Query("url") imageUrl:String
    ): TraceMoeResult

}