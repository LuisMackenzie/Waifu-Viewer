package com.mackenzie.waifuviewer.data.server

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Waifu(
    @SerializedName("dominant_color") val dominant_color: String,
    val extension: String,
    val favourites: Int,
    @SerializedName("file") val `file`: String,
    val height: String,
    @SerializedName("image_id") val imageId: Int,
    @SerializedName("is_nsfw") val isNsfw: Boolean,
    @SerializedName("preview_url") val previewUrl: String,
    val source: String,
    // val tags: List<Tag>,
    @SerializedName("uploaded_at") val uploadedAt: String,
    val url: String,
    val width: String
) : Parcelable


data class WaifuResult(
    @SerializedName("images")val waifus: List<Waifu>
)

@Parcelize
data class WaifuPic(
    @SerializedName("url")val url: String
) : Parcelable

data class WaifuPicsResult(
    @SerializedName("files")val images: List<String>
)

@Parcelize
data class Tag(
    val description: String,
    @SerializedName("is_nsfw") val isNsfw: Boolean,
    val name: String,
    @SerializedName("tag_id") val tagId: Int
) : Parcelable

data class TagResult(
    @SerializedName("nsfw") val categories: List<Tag>
)


