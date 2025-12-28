package com.mackenzie.waifuviewer.data.server

import arrow.core.Either
import com.mackenzie.waifuviewer.data.datasource.VideoHubRemoteDataSource
import com.mackenzie.waifuviewer.data.server.models.RemoteVideoHubConnect
import com.mackenzie.waifuviewer.data.server.models.videohub.CategoriesResponse
import com.mackenzie.waifuviewer.data.server.models.videohub.StarsDetailedResponse
import com.mackenzie.waifuviewer.data.server.models.videohub.StarsResponse
import com.mackenzie.waifuviewer.data.server.models.videohub.VideoActiveResponse
import com.mackenzie.waifuviewer.data.server.models.videohub.VideoByIdResponse
import com.mackenzie.waifuviewer.data.server.models.videohub.VideoEmbedCodeResponse
import com.mackenzie.waifuviewer.data.server.models.videohub.VideoSearchResponse
import com.mackenzie.waifuviewer.data.server.models.videohub.VideosDeletedResponse
import com.mackenzie.waifuviewer.data.server.models.videohub.TagsResponse
import com.mackenzie.waifuviewer.data.tryCall
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.video.TagInfo
import com.mackenzie.waifuviewer.domain.video.TagItem
import com.mackenzie.waifuviewer.domain.video.VideoListItem
import javax.inject.Inject

class VideoHubDataSource @Inject constructor(
    private val remoteService: RemoteVideoHubConnect
) : VideoHubRemoteDataSource {

    override suspend fun getVideoServer() = searchVideos()

    override suspend fun searchVideos(
        page: Int?,
        thumbsize: String?,
        search: String?,
        tags: List<String>?,
        stars: List<String>?,
        category: String?,
        ordering: String?,
        period: String?
    ): Either<Error, VideoListItem> = tryCall {
        remoteService.videoHubService
            .searchVideos(
                page = page,
                thumbsize = thumbsize,
                search = search,
                tags = tags,
                stars = stars,
                category = category,
                ordering = ordering,
                period = period
            ).toDomainModel()
    }

    override suspend fun getCategories() = tryCall {
        remoteService.videoHubService
            .getCategories()
            .toDomainModel()
    }

    override suspend fun getTags() = tryCall {
        remoteService.videoHubService
            .getTags()
            .toDomainModel()
    }

    override suspend fun getStars() = tryCall {
        remoteService.videoHubService
            .getStars()
            .toDomainModel()
    }

    override suspend fun getStarDetailedList() = tryCall {
        remoteService.videoHubService
            .getStarDetailedList()
            .toDomainModel()
    }

    override suspend fun isVideoActive(videoId: Int) = tryCall {
        remoteService.videoHubService
            .isVideoActive(videoid = videoId)
            .toDomainModel()
    }

    override suspend fun getVideoById(videoId: Int, thumbsize: String?) = tryCall {
        remoteService.videoHubService
            .getVideoById(videoid = videoId, thumbsize = thumbsize)
            .toDomainModel()
    }

    override suspend fun getVideoEmbedCode(videoId: Int) = tryCall {
        remoteService.videoHubService
            .getVideoEmbedCode(videoid = videoId)
            .toDomainModel()
    }

    override suspend fun areVideosDeleted(videoIds: List<Int>) = tryCall {
        remoteService.videoHubService
            .areVideosDeleted(videoids = videoIds.joinToString(","))
            .toDomainModel()
    }
}

private fun VideoSearchResponse.toDomainModel(): VideoListItem =
    VideoListItem(
        videos = videos.map { it.toDomainModel() },
        count = count
    )

private fun com.mackenzie.waifuviewer.data.server.models.videohub.Video.toDomainModel() =
    com.mackenzie.waifuviewer.domain.video.VideoItem(
        video = video.toDomainModel()
    )

private fun com.mackenzie.waifuviewer.data.server.models.videohub.VideoDetails.toDomainModel() =
    com.mackenzie.waifuviewer.domain.video.VideoItemDetails(
        videoId = videoId,
        title = title,
        thumb = thumb,
        url = url,
        publishDate = publishDate,
        rating = rating,
        ratings = ratings,
        views = views,
        duration = duration,
        defaultThumb = defaultThumb,
        thumbs = thumbs?.map {
            com.mackenzie.waifuviewer.domain.video.ThumbItem(
                size = it.size,
                width = it.width,
                height = it.height,
                src = it.src
            )
        },
        tags = tags?.map {
            com.mackenzie.waifuviewer.domain.video.TagItem(
                tagName = it.tagName
            )
        },
        stars = stars?.map {
            com.mackenzie.waifuviewer.domain.video.StarItem(
                star = com.mackenzie.waifuviewer.domain.video.StarInfoItem(
                    starName = it.star.starName,
                    starThumb = it.star.starThumb
                )
            )
        }
    )

private fun CategoriesResponse.toDomainModel(): CategoryList =
    CategoryList(categories = categories.map { CategorySummary(name = it.category) })

private fun TagsResponse.toDomainModel(): TagItem =
    TagItem(tag = tags.map { TagInfo(name = it.tag.tagName) })

private fun StarsResponse.toDomainModel(): StarList =
    StarList(stars = stars.map {
        StarSummary(
            name = it.star.starName,
            thumb = it.star.starThumb
        )
    })

private fun StarsDetailedResponse.toDomainModel(): StarListDetailed =
    StarListDetailed(stars = stars.map {
        StarItemSummary(
            name = it.star.starName,
            thumb = it.star.starThumb,
            url = it.star.starUrl,
            videosCount = it.star.videosCountAll
        )
    })

private fun VideoActiveResponse.toDomainModel(): VideoStatus =
    VideoStatus(isActive = active.active == "1")

private fun VideoByIdResponse.toDomainModel(): VideoById =
    VideoById(video = video.toDomainModel())

private fun VideoEmbedCodeResponse.toDomainModel(): VideoEmbed =
    VideoEmbed(embedCode = embed.code)

private fun VideosDeletedResponse.toDomainModel(): VideoStatus =
    VideoStatus(isActive = deleted.videos.any { it.deleted == "0" })
