package com.mackenzie.waifuviewer.data.server.mapper

import com.mackenzie.waifuviewer.data.server.models.ArtistImResult
import com.mackenzie.waifuviewer.data.server.models.Tag
import com.mackenzie.waifuviewer.data.server.models.WaifuIm
import com.mackenzie.waifuviewer.data.server.models.WaifuImTagResult
import com.mackenzie.waifuviewer.domain.im.ArtistIm
import com.mackenzie.waifuviewer.domain.im.TagItem
import com.mackenzie.waifuviewer.domain.im.WaifuImItem
import com.mackenzie.waifuviewer.domain.im.WaifuImTagList

/*fun WaifuImTagResult.toDomainModel(): WaifuImTagList = WaifuImTagList(
    versatile,
    nsfw
)

fun List<WaifuIm>.toDomainModel(): List<WaifuImItem> = map { it.toDomainModel() }

fun WaifuIm.toDomainModel(): WaifuImItem =
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

fun ArtistImResult.toArtistDomainModel(): ArtistIm = ArtistIm(
    artistId,
    deviantArt,
    name,
    patreon,
    pixiv,
    twitter
)

fun List<Tag>.toTagDomainModel() : List<TagItem> = map { it.toTagDomainModel()}


fun Tag.toTagDomainModel(): TagItem = TagItem(
    description,
    isNsfw,
    name,
    tagId
)*/
