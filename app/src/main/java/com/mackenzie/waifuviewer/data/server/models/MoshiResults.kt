package com.mackenzie.waifuviewer.data.server.models

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
data class WaifuImResult(
    @Json(name = "items") val waifus: List<WaifuIm>,
    @Json(name = "pageNumber") val pageNumber: Int,
    @Json(name = "totalPages") val totalPages: Int,
    @Json(name = "totalCount") val totalCount: Int,
    @Json(name = "maxPageSize") val maxPageSize: Int,
    @Json(name = "defaultPageSize") val defaultPageSize: Int,
    @Json(name = "hasPreviousPage") val hasPreviousPage: Boolean,
    @Json(name = "hasNextPage") val hasNextPage: Boolean
)

@JsonClass(generateAdapter = true)
data class WaifuImTagResult(
    @Json(name = "items") val categories: List<Tag>
)

@JsonClass(generateAdapter = true)
data class WaifuImTagFullResult(
    @Json(name = "versatile") val versatile: List<Tag>,
    @Json(name = "nsfw") val nsfw: List<Tag>
)

@JsonClass(generateAdapter = true)
data class WaifuPicsResult(
    @Json(name = "files") val images: List<String>
)

@JsonClass(generateAdapter = true)
data class WaifuPicsRequest(
    @Json(name = "classification") val classification: String,
    @Json(name = "category") val category: String
)

@JsonClass(generateAdapter = true)
data class WaifuBestPngResult(
    @Json(name = "results") val waifus: List<WaifuBestPng>
)

@JsonClass(generateAdapter = true)
data class WaifuBestGifResult(
    @Json(name = "results") val waifus: List<WaifuBestGif>
)

@Parcelize
@JsonClass(generateAdapter = true)
data class WaifuIm(
    @Json(name = "id") val imageId: Int,
    @Json(name = "perceptualHash") val perceptualHash: String,
    @Json(name = "extension") val extension: String,
    @Json(name = "dominantColor") val dominant_color: String,
    @Json(name = "source") val source: String?,
    @Json(name = "artists") val artists: List<ArtistImResult>?,
    @Json(name = "uploaderId") val uploadedId: String?,
    @Json(name = "uploadedAt") val uploadedAt: String?,
    @Json(name = "isNsfw") val isNsfw: Boolean?,
    @Json(name = "isAnimated") val isAnimated: Boolean?,
    @Json(name = "width") val width: Int?,
    @Json(name = "height") val height: Int?,
    @Json(name = "byteSize") val byteSize: Long?,
    @Json(name = "url") val url: String,
    @Json(name = "tags") val tags: List<Tag>?,
    @Json(name = "favorites") val favourites: Int?,
    @Json(name = "likedAt") val likedAt: String?,
    @Json(name = "addedToAlbumAt") val addedToAlbumAt: String?,
    @Json(name = "albums") val albums: List<String>
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class ArtistImResult(
    @Json(name = "id") val artistId: Int?,
    @Json(name = "deviantArt") val deviantArt: String?,
    @Json(name = "name") val name: String?,
    @Json(name = "patreon") val patreon: String?,
    @Json(name = "pixiv") val pixiv: String?,
    @Json(name = "twitter") val twitter: String?,
    @Json(name = "reviewStatus") val reviewStatus: String?,
    @Json(name = "creatorId") val creatorId: String?,
    @Json(name = "imageCount") val imageCount: Int?
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class WaifuPic(
    @Json(name = "url")val url: String
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class WaifuBestPng(
    @Json(name = "artist_href") val artistHref: String,
    @Json(name = "artist_name") val artistName: String,
    @Json(name = "source_url") val sourceUrl: String,
    @Json(name = "url") val url: String
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class WaifuBestGif(
    @Json(name = "anime_name") val animeName: String,
    @Json(name = "url") val url: String
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class Tag(
    @Json(name = "description") val description: String,
    @Json(name = "name") val name: String,
    @Json(name = "slug") val slug: String,
    @Json(name = "id") val tagId: Int,
    @Json(name = "reviewStatus") val reviewStatus: String?,
    @Json(name = "creatorId") val creatorId: String?,
    @Json(name = "imageCount") val imageCount: Int?
) : Parcelable

@JsonClass(generateAdapter = true)
data class TagResult(
    @Json(name = "nsfw") val categories: List<Tag>
)

@JsonClass(generateAdapter = true)
sealed class WaifuBestResult {
    data class WaifuBestPngResult(@Json(name = "results") val results: List<WaifuBestPng>)
    data class WaifuBestGifResult(@Json(name = "results") val results: List<WaifuBestGif>)
}

@JsonClass(generateAdapter = true)
data class TraceMoeResult(
    @Json(name = "frameCount") val frameCount: Int,
    @Json(name = "error") val error: String?,
    @Json(name = "result") val result: List<AnimeResult>
)

@JsonClass(generateAdapter = true)
data class AnimeResult(
    @Json(name = "anilist") val anilist: Int,
    @Json(name = "filename") val filename: String,
    @Json(name = "episode") val episode: Int?,
    @Json(name = "from") val from: Float?,
    @Json(name = "to") val to: Float,
    @Json(name = "similarity") val similarity: Float,
    @Json(name = "video") val video: String,
    @Json(name = "image") val image: String
)

