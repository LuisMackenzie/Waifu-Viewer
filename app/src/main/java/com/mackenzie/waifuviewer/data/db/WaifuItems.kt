package com.mackenzie.waifuviewer.data.db

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
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
    val tags: String,
    val isFavorite: Boolean
) : Parcelable

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
