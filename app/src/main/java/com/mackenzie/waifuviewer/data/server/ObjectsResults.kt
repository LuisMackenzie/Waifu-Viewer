package com.mackenzie.waifuviewer.data.server

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class WaifuIm(
    @SerializedName("signature") val signature: String,
    @SerializedName("extension") val extension: String,
    @SerializedName("image_id") val imageId: Int,
    @SerializedName("favourites") val favourites: Int,
    @SerializedName("dominant_color") val dominant_color: String,
    @SerializedName("source") val source: String,
    @SerializedName("uploaded_at") val uploadedAt: String,
    @SerializedName("is_nsfw") val isNsfw: Boolean,
    @SerializedName("width") val width: String,
    @SerializedName("height") val height: String,
    @SerializedName("url") val url: String,
    @SerializedName("preview_url") val previewUrl: String,
    // val tags: List<Tag>
) : Parcelable


data class WaifuResult(
    @SerializedName("images")val waifus: List<WaifuIm>
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

data class RemoteConnect(
    val serviceIm: WaifuImService,
    val servicePic: WaifuPicService
)


