package com.mackenzie.waifuviewer.data.datasource

import arrow.core.Either
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.video.VideoListItem
import com.mackenzie.waifuviewer.domain.video.category.CategoryList
import com.mackenzie.waifuviewer.domain.video.embed.VideoEmbed
import com.mackenzie.waifuviewer.domain.video.star.StarList
import com.mackenzie.waifuviewer.domain.video.star.StarListDetailed
import com.mackenzie.waifuviewer.domain.video.status.VideoStatus
import com.mackenzie.waifuviewer.domain.video.tag.TagList
import com.mackenzie.waifuviewer.domain.video.video.VideoById

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

    suspend fun getCategories(): Either<Error, CategoryList>

    suspend fun getTags(): Either<Error, TagList>

    suspend fun getStars(): Either<Error, StarList>

    suspend fun getStarDetailedList(): Either<Error, StarListDetailed>

    suspend fun isVideoActive(videoId: Int): Either<Error, VideoStatus>

    suspend fun getVideoById(videoId: Int, thumbsize: String? = null): Either<Error, VideoById>

    suspend fun getVideoEmbedCode(videoId: Int): Either<Error, VideoEmbed>

    suspend fun areVideosDeleted(videoIds: List<Int>): Either<Error, VideoStatus>

}