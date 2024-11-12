package com.mackenzie.waifuviewer.data.db

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mackenzie.waifuviewer.data.server.mapper.fromDomainModel
import com.mackenzie.waifuviewer.data.server.models.ArtistImResult
import com.mackenzie.waifuviewer.data.server.models.Tag
import com.mackenzie.waifuviewer.domain.im.ArtistIm
import com.mackenzie.waifuviewer.domain.im.TagItem
import com.mackenzie.waifuviewer.domain.im.WaifuImItem
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class WaifuImDbItem(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val artist: String,
    val byteSize: Long,
    val signature: String,
    val extension: String,
    val dominantColor: String,
    val source: String,
    val uploadedAt: String,
    val isNsfw: Boolean,
    val width: String,
    val height: String,
    val imageId: Int,
    val url: String,
    val previewUrl: String,
    val tags: List<String>,
    val isFavorite: Boolean
) : Parcelable {

    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val artistAdapter = moshi.adapter(ArtistIm::class.java)
    private val tagsAdapter = moshi.adapter(TagItem::class.java)

    private fun List<WaifuImDbItem>.toDomainModel(): List<WaifuImItem> = map { it.toDomainModel() }

    private fun WaifuImDbItem.toDomainModel(): WaifuImItem =
        WaifuImItem(
            id,
            artistAdapter.fromJson(artist) ?: ArtistIm("", "", "", "", "", ""),
            byteSize,
            signature,
            extension,
            dominantColor,
            source,
            uploadedAt,
            isNsfw,
            width,
            height,
            imageId,
            url,
            previewUrl,
            tags.map { tagsAdapter.fromJson(it) ?: TagItem("", false, "", 0) },
            isFavorite
        )

    fun List<WaifuImItem>.fromDomainModel(): List<WaifuImDbItem> = map { it.fromDomainModel() }

    fun WaifuImItem.fromDomainModel(): WaifuImDbItem = WaifuImDbItem(
        id,
        artistAdapter.toJson(artist) ?: "",
        byteSize,
        signature,
        extension,
        dominantColor,
        source,
        uploadedAt,
        isNsfw,
        width,
        height,
        imageId,
        url,
        previewUrl,
        tags.map { tagsAdapter.toJson(it) ?: "" },
        isFavorite
    )

}

/*@Parcelize
@Entity
data class ArtistImDb(
    val artistId: String,
    val deviantArt: String,
    val name: String,
    val patreon: String,
    val pixiv: String,
    val twitter: String
) : Parcelable*/

/*@Parcelize
@Entity
data class TagDb(
    val description: String,
    val isNsfw: Boolean,
    val name: String,
    val tagId: Int
) : Parcelable*/

@Parcelize
@Entity
data class WaifuPicDbItem(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val url: String,
    val isFavorite: Boolean
) : Parcelable

@Parcelize
@Entity
data class WaifuBestDbItem(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val artistHref: String,
    val artistName: String,
    val animeName: String,
    val sourceUrl: String,
    val url: String,
    val isFavorite: Boolean
) : Parcelable

/*@Parcelize
@Entity
data class WaifuBestPngDbItem(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val artistHref: String,
    val artistName: String,
    val sourceUrl: String,
    val url: String,
    val isFavorite: Boolean
) : Parcelable*/

/*@Parcelize
@Entity
data class WaifuBestGifDbItem(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val animeName: String,
    val url: String,
    val isFavorite: Boolean
) : Parcelable*/

@Parcelize
@Entity
data class FavoriteDbItem(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val url: String,
    val title: String,
    val isFavorite: Boolean
) : Parcelable
