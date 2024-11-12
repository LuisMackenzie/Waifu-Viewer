package com.mackenzie.waifuviewer.data.db.datasources

import com.mackenzie.waifuviewer.data.datasource.WaifusImLocalDataSource
import com.mackenzie.waifuviewer.data.db.WaifuImDao
import com.mackenzie.waifuviewer.data.db.WaifuImDbItem
import com.mackenzie.waifuviewer.data.db.datasources.RoomImDataSource.Companion.artistAdapter
import com.mackenzie.waifuviewer.data.db.datasources.RoomImDataSource.Companion.tagsAdapter
import com.mackenzie.waifuviewer.data.tryCall
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.im.ArtistIm
import com.mackenzie.waifuviewer.domain.im.TagItem
import com.mackenzie.waifuviewer.domain.im.WaifuImItem
import com.mackenzie.waifuviewer.domain.im.WaifuImTagList
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoomImDataSource @Inject constructor(private val imDao: WaifuImDao) : WaifusImLocalDataSource {

    companion object {
        private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val artistAdapter = moshi.adapter(ArtistIm::class.java)
        private val type = Types.newParameterizedType(List::class.java, TagItem::class.java)
        val tagsAdapter = moshi.adapter<List<TagItem?>>(type)
    }

    override val waifusIm: Flow<List<WaifuImItem>> = imDao.getAllIm().map { it.toDomainModel() }

    override val waifuImTags: Flow<WaifuImTagList>
        get() = TODO("Not yet implemented")

    override suspend fun isImEmpty(): Boolean = imDao.waifuImCount() == 0

    override fun findImById(id: Int): Flow<WaifuImItem> = imDao.findImById(id).map { it.toDomainModel() }

    override suspend fun saveIm(waifus: List<WaifuImItem>): Error? = tryCall {
        imDao.insertAllWaifuIm(waifus.fromDomainModel())
    }.fold(ifLeft = { it }, ifRight = { null })

    override suspend fun saveImTags(waifus: WaifuImTagList): Error? {
        TODO("Not yet implemented")
    }

    override suspend fun saveOnlyIm(waifu: WaifuImItem): Error? = tryCall {
        imDao.updateWaifuIm(waifu.fromDomainModel())
       /* if (!waifu.isFavorite) {
            favDao.deleteFav(FavoriteDbItem(waifu.id, waifu.url, waifu.imageId.toString(), waifu.isFavorite))

        } else {
            imDao.updateWaifuIm(waifu.fromDomainModel())
        }*/
    }.fold(ifLeft = { it }, ifRight = { null })

    override suspend fun deleteAll(): Error? = tryCall {
        imDao.deleteAll()
    }.fold(ifLeft = { it }, ifRight = { null })

}

private fun List<WaifuImDbItem>.toDomainModel(): List<WaifuImItem> = map { it.toDomainModel() }

private fun WaifuImDbItem.toDomainModel(): WaifuImItem =
    WaifuImItem(
        id,
        artist.getArtistToDomainModel(),
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
        tagsAdapter.fromJson(tags) ?: emptyList(),
        isFavorite
    )

private fun String.getArtistToDomainModel() :ArtistIm {
    val artist = artistAdapter.fromJson(this)
    return ArtistIm(
        artist?.artistId?.ifBlank { null },
        artist?.deviantArt?.ifBlank { null },
        artist?.name?.ifBlank { null },
        artist?.patreon?.ifBlank { null },
        artist?.pixiv?.ifBlank { null },
        artist?.twitter?.ifBlank { null }
    )
}

fun List<WaifuImItem>.fromDomainModel(): List<WaifuImDbItem> = map { it.fromDomainModel() }

private fun WaifuImItem.fromDomainModel(): WaifuImDbItem = WaifuImDbItem(
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
    tagsAdapter.toJson(tags),
    isFavorite
)





