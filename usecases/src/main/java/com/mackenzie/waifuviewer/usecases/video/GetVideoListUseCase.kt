package com.mackenzie.waifuviewer.usecases.video

import arrow.core.Either
import com.mackenzie.waifuviewer.data.VideoHubRepository
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.video.VideoListItem
import javax.inject.Inject

class GetVideoListUseCase @Inject constructor(private val repo: VideoHubRepository) {

    suspend operator fun invoke(
        page: Int?,
        thumbsize: String?,
        search: String?,
        tags: List<String>?,
        stars: List<String>?,
        category: String?,
        ordering: String?,
        period: String?
    ):  Either<Error, VideoListItem> = repo.requestVideoList(page, thumbsize, search, tags, stars, category, ordering, period)

}