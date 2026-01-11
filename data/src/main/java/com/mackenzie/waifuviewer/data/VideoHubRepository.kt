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

    suspend fun requestVideoList(
        page: Int?,
        thumbsize: String?,
        search: String?,
        tags: List<String>?,
        stars: List<String>?,
        category: String?,
        ordering: String?,
        period: String?
    ):  Either<Error, VideoListItem> {

        return remoteDataSource.searchVideos(
            page,
            thumbsize,
            search,
            tags,
            stars,
            category,
            ordering,
            period
        )
    }

    suspend fun requestDefaultVideoList():  Either<Error, VideoListItem> {

        return remoteDataSource.searchVideos()
    }


}