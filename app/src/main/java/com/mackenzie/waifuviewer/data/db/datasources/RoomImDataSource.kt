package com.mackenzie.waifuviewer.data.db.datasources

import com.mackenzie.waifuviewer.data.datasource.WaifusImLocalDataSource
import com.mackenzie.waifuviewer.data.db.WaifuImDao
import com.mackenzie.waifuviewer.data.server.mapper.fromDomainModel
import com.mackenzie.waifuviewer.data.server.mapper.toDomainModel
import com.mackenzie.waifuviewer.data.tryCall
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.im.WaifuImItem
import com.mackenzie.waifuviewer.domain.im.WaifuImTagList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoomImDataSource @Inject constructor(private val imDao: WaifuImDao) : WaifusImLocalDataSource {

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

/*private fun List<WaifuImDbItem>.toDomainModel(): List<WaifuImItem> = map { it.toDomainModel() }

private fun WaifuImDbItem.toDomainModel(): WaifuImItem =
    WaifuImItem(
        id,
        artist,
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
        tags,
        isFavorite
    )*/

/*fun ArtistImDb.toDomainModel(): ArtistIm = ArtistIm(
    artistId,
    deviantArt,
    name,
    patreon,
    pixiv,
    twitter
)

private fun List<TagDb>.toDomainModel() : List<TagItem> = map { it.toDomainModel()}

private fun TagDb.toDomainModel(): TagItem = TagItem(
    description,
    isNsfw,
    name,
    tagId
)*/




