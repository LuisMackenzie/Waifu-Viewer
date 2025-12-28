package com.mackenzie.waifuviewer.data.datasource

import arrow.core.Either
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.TextCompletionItemResponse
import com.mackenzie.waifuviewer.domain.video.ServersVideoHubItemResponse

interface VideoHubRemoteDataSource {

    suspend fun getVideoServer(
        apiKey: String,
    ): Either<Error, ServersVideoHubItemResponse?>

    suspend fun getVideoServer2(
        apiKey: String,
    ): Either<Error, String?>

}