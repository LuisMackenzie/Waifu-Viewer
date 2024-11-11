package com.mackenzie.waifuviewer.data.server.models

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
data class WaifuImResult(
    @Json(name = "images") val waifus: List<WaifuIm>
)

@JsonClass(generateAdapter = true)
data class WaifuImTagResult(
    @Json(name = "versatile") val versatile: List<String>,
    @Json(name = "nsfw") val nsfw: List<String>
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
    @Json(name = "signature") val signature: String,
    @Json(name = "extension") val extension: String,
    @Json(name = "image_id") val imageId: Int,
    @Json(name = "favorites") val favourites: Int,
    @Json(name = "dominant_color") val dominant_color: String,
    @Json(name = "source") val source: String?,
    @Json(name = "uploaded_at") val uploadedAt: String,
    @Json(name = "liked_at") val likedAt: String?,
    @Json(name = "is_nsfw") val isNsfw: Boolean,
    @Json(name = "width") val width: String,
    @Json(name = "height") val height: String,
    @Json(name = "url") val url: String,
    @Json(name = "preview_url") val previewUrl: String,
    // @Json(name = "tags")val tags: List<Tag>?
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
    @Json(name = "is_nsfw") val isNsfw: Boolean,
    @Json(name = "name") val name: String,
    @Json(name = "tag_id") val tagId: Int
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

