package com.mackenzie.waifuviewer.usecases.video

import arrow.core.Either
import com.mackenzie.waifuviewer.data.VideoHubRepository
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.video.VideoListItem
import javax.inject.Inject

class GetBeegVideoListUseCase @Inject constructor(private val repo: VideoHubRepository) {

    suspend operator fun invoke(serverId: Int, serverUrl: String):  Either<Error, VideoListItem> = repo.primaryVideoScrapper(serverId, serverUrl)

}