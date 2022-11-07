package com.mackenzie.waifuviewer.data.server

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

/*data class WaifuImResult(
    @SerializedName("images") val waifus: List<WaifuIm>
)*/

/*data class WaifuPicsResult(
    @SerializedName("files") val images: List<String>
)*/

/*data class WaifuBestPngResult(
    @SerializedName("results") val waifus: List<WaifuBestPng>
)

data class WaifuBestGifResult(
    @SerializedName("results") val waifus: List<WaifuBestGif>
)*/

/*@Parcelize
data class WaifuIm(
    @SerializedName("signature") val signature: String,
    @SerializedName("extension") val extension: String,
    @SerializedName("image_id") val imageId: Int,
    @SerializedName("favourites") val favourites: Int,
    @SerializedName("dominant_color") val dominant_color: String,
    @SerializedName("source") val source: String?,
    @SerializedName("uploaded_at") val uploadedAt: String,
    @SerializedName("is_nsfw") val isNsfw: Boolean,
    @SerializedName("width") val width: String,
    @SerializedName("height") val height: String,
    @SerializedName("url") val url: String,
    @SerializedName("preview_url") val previewUrl: String,
    // val tags: List<Tag>
) : Parcelable*/

/*@Parcelize
data class WaifuPic(
    @SerializedName("url")val url: String
) : Parcelable*/

/*@Parcelize
data class WaifuBestPng(
    @SerializedName("artist_href") val artistHref: String,
    @SerializedName("artist_name") val artistName: String,
    @SerializedName("source_url") val sourceUrl: String,
    @SerializedName("url") val url: String
) : Parcelable

@Parcelize
data class WaifuBestGif(
    @SerializedName("anime_name") val animeName: String,
    @SerializedName("url") val url: String
) : Parcelable*/

@Parcelize
data class CategoriesResult(
    @SerializedName("baka")
    val baka: CategoryContent,
    @SerializedName("bite")
    val bite: CategoryContent,
    @SerializedName("blush")
    val blush: CategoryContent,
    @SerializedName("bored")
    val bored: CategoryContent,
    @SerializedName("cry")
    val cry: CategoryContent,
    @SerializedName("cuddle")
    val cuddle: CategoryContent,
    @SerializedName("dance")
    val dance: CategoryContent,
    @SerializedName("facepalm")
    val facepalm: CategoryContent,
    @SerializedName("feed")
    val feed: CategoryContent,
    @SerializedName("handhold")
    val handhold: CategoryContent,
    @SerializedName("happy")
    val happy: CategoryContent,
    @SerializedName("highfive")
    val highfive: CategoryContent,
    @SerializedName("hug")
    val hug: CategoryContent,
    @SerializedName("husbando")
    val husbando: CategoryContent,
    @SerializedName("kick")
    val kick: CategoryContent,
    @SerializedName("kiss")
    val kiss: CategoryContent,
    @SerializedName("kitsune")
    val kitsune: CategoryContent,
    @SerializedName("laugh")
    val laugh: CategoryContent,
    @SerializedName("neko")
    val neko: CategoryContent,
    @SerializedName("pat")
    val pat: CategoryContent,
    @SerializedName("poke")
    val poke: CategoryContent,
    @SerializedName("pout")
    val pout: CategoryContent,
    @SerializedName("punch")
    val punch: CategoryContent,
    @SerializedName("shoot")
    val shoot: CategoryContent,
    @SerializedName("shrug")
    val shrug: CategoryContent,
    @SerializedName("slap")
    val slap: CategoryContent,
    @SerializedName("sleep")
    val sleep: CategoryContent,
    @SerializedName("smile")
    val smile: CategoryContent,
    @SerializedName("smug")
    val smug: CategoryContent,
    @SerializedName("stare")
    val stare: CategoryContent,
    @SerializedName("think")
    val think: CategoryContent,
    @SerializedName("thumbsup")
    val thumbsup: CategoryContent,
    @SerializedName("tickle")
    val tickle: CategoryContent,
    @SerializedName("waifu")
    val waifu: CategoryContent,
    @SerializedName("wave")
    val wave: CategoryContent,
    @SerializedName("wink")
    val wink: CategoryContent,
    @SerializedName("yeet")
    val yeet: CategoryContent
) : Parcelable

@Parcelize
data class CategoryContent(
    @SerializedName("format")
    val format: String,
    @SerializedName("max")
    val max: String,
    @SerializedName("min")
    val min: String
) : Parcelable

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
    val servicePic: WaifuPicService,
    val serviceBest: WaifuBestService
)

@Serializable
sealed class WaifuBestResult {
    data class WaifuBestPngResult(val results: List<WaifuBestPng>)
    data class WaifuBestGifResult(val results: List<WaifuBestGif>)
}





