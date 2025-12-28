package com.mackenzie.waifuviewer.data.server

import arrow.core.Either
import com.mackenzie.waifuviewer.data.datasource.OpenAiRemoteDataSource
import com.mackenzie.waifuviewer.data.datasource.VideoHubRemoteDataSource
import com.mackenzie.waifuviewer.data.server.models.RemoteConnect
import com.mackenzie.waifuviewer.data.server.models.RemoteVideoHubConnect
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.TextCompletionItemResponse
import com.mackenzie.waifuviewer.domain.video.ServersVideoHubItemResponse
import javax.inject.Inject

class VideoHubDataSource @Inject constructor(private val remoteService: RemoteVideoHubConnect): VideoHubRemoteDataSource {

    override suspend fun getVideoServer(apiKey: String): Either<Error, ServersVideoHubItemResponse?> {
        TODO("Not yet implemented")
    }

    override suspend fun getVideoServer2(apiKey: String): Either<Error, String?> {
        TODO("Not yet implemented")
    }
}