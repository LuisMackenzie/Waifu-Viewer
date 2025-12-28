package com.mackenzie.waifuviewer.data.server

import arrow.core.Either
import com.mackenzie.waifuviewer.data.datasource.VideoHubRemoteDataSource
import com.mackenzie.waifuviewer.data.server.models.RemoteVideoHubConnect
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.video.VideoListItem
import javax.inject.Inject

class VideoHubDataSource @Inject constructor(private val remoteService: RemoteVideoHubConnect): VideoHubRemoteDataSource {

    override suspend fun getVideoServer(): Either<Error, VideoListItem?> {
        TODO("Not yet implemented")
    }
}