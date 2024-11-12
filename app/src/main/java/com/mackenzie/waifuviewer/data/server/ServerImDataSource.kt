package com.mackenzie.waifuviewer.data.server

import arrow.core.Either
import com.mackenzie.waifuviewer.data.datasource.WaifusImRemoteDataSource
import com.mackenzie.waifuviewer.data.server.models.ArtistImResult
import com.mackenzie.waifuviewer.data.server.models.RemoteConnect
import com.mackenzie.waifuviewer.data.server.models.Tag
import com.mackenzie.waifuviewer.data.server.models.WaifuIm
import com.mackenzie.waifuviewer.data.server.models.WaifuImTagResult
import com.mackenzie.waifuviewer.data.tryCall
import com.mackenzie.waifuviewer.data.trySave
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.im.ArtistIm
import com.mackenzie.waifuviewer.domain.im.TagItem
import com.mackenzie.waifuviewer.domain.im.WaifuImItem
import com.mackenzie.waifuviewer.domain.im.WaifuImTagList
import javax.inject.Inject


class ServerImDataSource @Inject constructor(private val remoteService: RemoteConnect): WaifusImRemoteDataSource {

    override suspend fun getRandomWaifusIm(
        isNsfw: Boolean, tag: String,
        isGif: Boolean, orientation: String
    ): Either<Error, List<WaifuImItem>> = tryCall {
        remoteService.serviceIm
            .getRandomWaifuIm(isNsfw, tag, isGif,  orientation)
            .waifus
            .toDomainModel()
    }

    override suspend fun getOnlyWaifuIm(orientation: String): WaifuImItem? = trySave {
        remoteService.serviceIm
            .getOnlyRandomWaifuIm(orientation = orientation)
            .waifus
            .firstOrNull()?.toDomainModel()
    }

    override suspend fun getWaifuImTags(): WaifuImTagList? = trySave {
        remoteService.serviceIm
            .getTagsWaifuIm()
            .toDomainModel()
    }

}

private fun WaifuImTagResult.toDomainModel(): WaifuImTagList = WaifuImTagList(
    versatile,
    nsfw
)

private fun List<WaifuIm>.toDomainModel(): List<WaifuImItem> = map { it.toDomainModel() }

private fun WaifuIm.toDomainModel(): WaifuImItem =
    WaifuImItem(
        0,
        artist?.toArtistDomainModel() ?: ArtistIm("", "", "", "", "", ""),
        byteSize,
        signature,
        extension,
        dominant_color,
        source ?: "",
        uploadedAt,
        isNsfw,
        width,
        height,
        imageId,
        url,
        previewUrl,
        tags?.toTagDomainModel() ?: emptyList(),
        false
    )

private fun ArtistImResult.toArtistDomainModel(): ArtistIm = ArtistIm(
    artistId,
    deviantArt,
    name,
    patreon,
    pixiv,
    twitter
)

private fun List<Tag>.toTagDomainModel() : List<TagItem> = map { it.toTagDomainModel()}

private fun Tag.toTagDomainModel(): TagItem = TagItem(
    description,
    isNsfw,
    name,
    tagId
)


