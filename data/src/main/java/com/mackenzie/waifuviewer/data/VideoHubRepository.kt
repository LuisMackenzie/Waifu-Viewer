package com.mackenzie.waifuviewer.data

import arrow.core.Either
import com.mackenzie.waifuviewer.data.datasource.VideoHubLocalDataSource
import com.mackenzie.waifuviewer.data.datasource.VideoHubRemoteDataSource
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.video.VideoListItem
import javax.inject.Inject

class VideoHubRepository @Inject constructor(
    // val localDataSource: VideoHubLocalDataSource,
    val remoteDataSource: VideoHubRemoteDataSource
) {

    suspend fun requestVideoSources(): Error? {
        val videoList = remoteDataSource.getVideoServer()
            .fold(ifLeft = { return it }) {
                // save to database
                // Guardar en DBBD

            }


        return null
    }

    suspend fun requestVideoSources2():  Either<Error, VideoListItem> {

        return remoteDataSource.getVideoServer()
    }


}