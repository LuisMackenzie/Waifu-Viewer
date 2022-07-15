package com.mackenzie.waifuviewer.models.db

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mackenzie.waifuviewer.models.Tag
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class WaifuImItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val dominant_color: String,
    val extension: String,
    val favourites: Int,
    val file: String,
    val height: String,
    val imageId: Int,
    val isNsfw: Boolean,
    val previewUrl: String,
    val source: String,
    // val tags: List<Tag>,
    val uploadedAt: String,
    val url: String,
    val width: String
) : Parcelable

@Parcelize
@Entity
data class WaifuPicItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val url: String
) : Parcelable
