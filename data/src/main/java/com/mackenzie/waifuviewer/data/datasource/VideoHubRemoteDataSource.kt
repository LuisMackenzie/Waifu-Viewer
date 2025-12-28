package com.mackenzie.waifuviewer.data.datasource

import arrow.core.Either
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.video.CategoriesItem
import com.mackenzie.waifuviewer.domain.video.StarsDetailedItems
import com.mackenzie.waifuviewer.domain.video.StarsItems
import com.mackenzie.waifuviewer.domain.video.TagsResponseItem
import com.mackenzie.waifuviewer.domain.video.VideoActiveItems
import com.mackenzie.waifuviewer.domain.video.VideoByIdItem
import com.mackenzie.waifuviewer.domain.video.VideoEmbedCodeItem
import com.mackenzie.waifuviewer.domain.video.VideoListItem
import com.mackenzie.waifuviewer.domain.video.VideosDeletedItem

interface VideoHubRemoteDataSource {

    suspend fun getVideoServer(): Either<Error, VideoListItem>

    suspend fun searchVideos(
        page: Int? = null,
        thumbsize: String? = null,
        search: String? = null,
        tags: List<String>? = null,
        stars: List<String>? = null,
        category: String? = null,
        ordering: String? = null,
        period: String? = null
    ): Either<Error, VideoListItem>

    suspend fun getCategories(): Either<Error, CategoriesItem>

    suspend fun getTags(): Either<Error, TagsResponseItem>

    suspend fun getStars(): Either<Error, StarsItems>

    suspend fun getStarDetailedList(): Either<Error, StarsDetailedItems>

    suspend fun isVideoActive(videoId: Int): Either<Error, VideoActiveItems>

    suspend fun getVideoById(videoId: Int, thumbsize: String? = null): Either<Error, VideoByIdItem>

    suspend fun getVideoEmbedCode(videoId: Int): Either<Error, VideoEmbedCodeItem>

    suspend fun areVideosDeleted(videoIds: List<Int>): Either<Error, VideosDeletedItem>

}