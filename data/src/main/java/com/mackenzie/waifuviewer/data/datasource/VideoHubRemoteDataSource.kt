package com.mackenzie.waifuviewer.data.datasource

import arrow.core.Either
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.video.VideoListItem

interface VideoHubRemoteDataSource {

    suspend fun getVideoServer(): Either<Error, VideoListItem?>

}