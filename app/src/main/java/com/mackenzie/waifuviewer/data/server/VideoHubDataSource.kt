package com.mackenzie.waifuviewer.data.server

import arrow.core.Either
import com.mackenzie.waifuviewer.data.datasource.VideoHubRemoteDataSource
import com.mackenzie.waifuviewer.data.server.models.RemoteVideoHubConnect
import com.mackenzie.waifuviewer.data.server.models.videohub.CategoriesResponse
import com.mackenzie.waifuviewer.data.server.models.videohub.CategoryResponse
import com.mackenzie.waifuviewer.data.server.models.videohub.DeletedVideo
import com.mackenzie.waifuviewer.data.server.models.videohub.DetailedStarInfo
import com.mackenzie.waifuviewer.data.server.models.videohub.EmbedInfo
import com.mackenzie.waifuviewer.data.server.models.videohub.StarsDetailedResponse
import com.mackenzie.waifuviewer.data.server.models.videohub.StarsResponse
import com.mackenzie.waifuviewer.data.server.models.videohub.TagItem
import com.mackenzie.waifuviewer.data.server.models.videohub.TagsResponse
import com.mackenzie.waifuviewer.data.server.models.videohub.VideoActiveResponse
import com.mackenzie.waifuviewer.data.server.models.videohub.VideoByIdResponse
import com.mackenzie.waifuviewer.data.server.models.videohub.VideoDetails
import com.mackenzie.waifuviewer.data.server.models.videohub.VideoDetailsById
import com.mackenzie.waifuviewer.data.server.models.videohub.VideoEmbedCodeResponse
import com.mackenzie.waifuviewer.data.server.models.videohub.VideoResponse
import com.mackenzie.waifuviewer.data.server.models.videohub.VideoSearchResponse
import com.mackenzie.waifuviewer.data.server.models.videohub.VideosDeletedResponse
import com.mackenzie.waifuviewer.data.tryCall
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.VideoItem
import com.mackenzie.waifuviewer.domain.video.ActiveInfo
import com.mackenzie.waifuviewer.domain.video.CategoriesItem
import com.mackenzie.waifuviewer.domain.video.CategoryItem
import com.mackenzie.waifuviewer.domain.video.DeletedInfoItem
import com.mackenzie.waifuviewer.domain.video.DeletedVideoItem
import com.mackenzie.waifuviewer.domain.video.DetailedStarDomainItem
import com.mackenzie.waifuviewer.domain.video.DetailedStarInfoItem
import com.mackenzie.waifuviewer.domain.video.EmbedInfoItem
import com.mackenzie.waifuviewer.domain.video.StarBasicDomainInfo
import com.mackenzie.waifuviewer.domain.video.StarDomainItem
import com.mackenzie.waifuviewer.domain.video.StarInfoItem
import com.mackenzie.waifuviewer.domain.video.StarItem
import com.mackenzie.waifuviewer.domain.video.StarsDetailedItems
import com.mackenzie.waifuviewer.domain.video.StarsItems
import com.mackenzie.waifuviewer.domain.video.TagDomainInfo
import com.mackenzie.waifuviewer.domain.video.TagDomainItem
import com.mackenzie.waifuviewer.domain.video.TagsResponseItem
import com.mackenzie.waifuviewer.domain.video.ThumbItem
import com.mackenzie.waifuviewer.domain.video.VideoActiveItems
import com.mackenzie.waifuviewer.domain.video.VideoByIdItem
import com.mackenzie.waifuviewer.domain.video.VideoDetailsByIdItem
import com.mackenzie.waifuviewer.domain.video.VideoEmbedCodeItem
import com.mackenzie.waifuviewer.domain.video.VideoItemDetails
import com.mackenzie.waifuviewer.domain.video.VideoListItem
import com.mackenzie.waifuviewer.domain.video.VideosDeletedItem
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

    override suspend fun getCategories(): Either<Error, CategoriesItem> = tryCall {
        remoteService.videoHubService
            .getCategories()
            .toDomainModel()
    }

    override suspend fun getTags(): Either<Error, TagsResponseItem> = tryCall {
        remoteService.videoHubService
            .getTags()
            .toDomainModel()
    }

    override suspend fun getStars(): Either<Error, StarsItems> = tryCall {
        remoteService.videoHubService
            .getStars()
            .toDomainModel()
    }

    override suspend fun getStarDetailedList(): Either<Error, StarsDetailedItems> = tryCall {
        remoteService.videoHubService
            .getStarDetailedList()
            .toDomainModel()
    }

    override suspend fun isVideoActive(videoId: Int): Either<Error, VideoActiveItems> = tryCall {
        remoteService.videoHubService
            .isVideoActive(videoid = videoId)
            .toDomainModel()
    }

    override suspend fun getVideoById(videoId: Int, thumbsize: String?): Either<Error, VideoByIdItem> = tryCall {
        remoteService.videoHubService
            .getVideoById(videoid = videoId, thumbsize = thumbsize)
            .toDomainModel()
    }

    override suspend fun getVideoEmbedCode(videoId: Int): Either<Error, VideoEmbedCodeItem> = tryCall {
        remoteService.videoHubService
            .getVideoEmbedCode(videoid = videoId)
            .toDomainModel()
    }

    override suspend fun areVideosDeleted(videoIds: List<Int>): Either<Error, VideosDeletedItem> = tryCall {
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

private fun VideoResponse.toDomainModel() =
    com.mackenzie.waifuviewer.domain.video.VideoDomainItem(
        video = video.toDomainModel()
    )

private fun VideoDetails.toDomainModel() =
    VideoItemDetails(
        videoId = videoId,
        title = title,
        thumb = thumb,
        url = url,
        embedUrl = embedUrl,
        publishDate = publishDate,
        rating = rating,
        ratings = ratings,
        views = views,
        duration = duration,
        defaultThumb = defaultThumb,
        type = "VIDEO",
        thumbs = thumbs?.map {
            ThumbItem(
                size = it.size,
                width = it.width,
                height = it.height,
                src = it.src
            )
        },
        tags = tags?.map {
            TagDomainInfo(tagName = it.tagName)
        },
        stars = stars?.map {
            StarItem(
                star = StarInfoItem(
                    starName = it.star.starName,
                    starThumb = it.star.starThumb ?: ""
                )
            )
        }
    )

private fun CategoriesResponse.toDomainModel(): CategoriesItem =
    CategoriesItem(categories = categories.toDomainCategoryModel(), count = count)

private fun List<CategoryResponse>.toDomainCategoryModel(): List<CategoryItem> =
    this.map { CategoryItem(category = it.category) }

private fun TagsResponse.toDomainModel(): TagsResponseItem =
    TagsResponseItem(tags = tags.toDomainTagItemModel(), count = count)

private fun List<TagItem>.toDomainTagItemModel(): List<TagDomainItem> =
    this.map { TagDomainItem(tag = TagDomainInfo(tagName = it.tag.tagName)) }

private fun StarsResponse.toDomainModel(): StarsItems =
    StarsItems(
        stars = stars.map { StarDomainItem(
            star = StarBasicDomainInfo(
                starName = it.star.starName,
                starThumb = it.star.starThumb)
        ) },
        count = count)


/*private fun StarBasicInfo.toDomainModel(): StarBasicDomainInfo =
    StarBasicDomainInfo(starName = starName, starThumb = starThumb)  */

private fun StarsDetailedResponse.toDomainModel(): StarsDetailedItems =
    StarsDetailedItems(
        stars = stars.map { it.star.toDomainModel() },
        count = count
    )

private fun DetailedStarInfo.toDomainModel(): DetailedStarDomainItem =
    DetailedStarDomainItem(
        star = DetailedStarInfoItem(
            starName = starName,
            starThumb = starThumb,
            starUrl = starUrl,
            videosCountAll = videosCountAll
        )
    )

private fun VideoActiveResponse.toDomainModel(): VideoActiveItems =
    VideoActiveItems(active = ActiveInfo(active.active))

private fun VideoByIdResponse.toDomainModel(): VideoByIdItem =
    VideoByIdItem(video = video.toDomainModel())

private fun VideoDetailsById.toDomainModel(): VideoDetailsByIdItem =
    VideoDetailsByIdItem(
        videoId = videoId,
        title = title,
        thumb = thumb,
        url = url,
        embedUrl = embedUrl,
        publishDate = publishDate,
        rating = rating,
        ratings = ratings,
        views = views,
        duration = duration,
        defaultThumb = defaultThumb,
        thumbs = thumbs?.map {
            ThumbItem(
                size = it.size,
                width = it.width,
                height = it.height,
                src = it.src
            )
        },
        tags = tags?.map {
            TagDomainItem(
                tag = TagDomainInfo(it.tagName)
            )
        },
        stars = stars?.map {
            StarItem(
                star = StarInfoItem(
                    starName = it.star.starName,
                    starThumb = it.star.starThumb ?: ""
                )
            )
        }
    )

private fun VideoEmbedCodeResponse.toDomainModel(): VideoEmbedCodeItem =
    VideoEmbedCodeItem(embed = embed.toDomainModel())

private fun EmbedInfo.toDomainModel()= EmbedInfoItem(
    code = code,
)

private fun VideosDeletedResponse.toDomainModel(): VideosDeletedItem =
    VideosDeletedItem(deleted = DeletedInfoItem(count= deleted.count, videos = deleted.videos.map { it.toDomainModel() }))

private fun DeletedVideo.toDomainModel(): DeletedVideoItem =
    DeletedVideoItem(
        videoId = videoId,
        deleted = deleted
    )
