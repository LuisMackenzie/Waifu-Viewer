package com.mackenzie.waifuviewer.framework.db

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class WaifuImDbItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val dominant_color: String,
    // val extension: String,
    // val favourites: Int,
    val file: String,
    val height: String,
    val imageId: Int,
    val isNsfw: Boolean,
    // val previewUrl: String,
    // val source: String,
    // val tags: List<Tag>,
    // val uploadedAt: String,
    val url: String,
    val width: String,
    val isFavorite: Boolean
) : Parcelable

@Parcelize
@Entity
data class WaifuPicDbItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val url: String,
    val isFavorite: Boolean
) : Parcelable
