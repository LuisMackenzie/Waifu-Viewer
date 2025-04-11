package com.mackenzie.waifuviewer.data.db.datasources

import com.mackenzie.waifuviewer.data.datasource.FavoriteLocalDataSource
import com.mackenzie.waifuviewer.data.db.dao.FavoriteDao
import com.mackenzie.waifuviewer.data.db.FavoriteDbItem
import com.mackenzie.waifuviewer.data.tryCall
import com.mackenzie.waifuviewer.domain.*
import com.mackenzie.waifuviewer.domain.im.WaifuImItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavoriteDataSource @Inject constructor(private val favDao: FavoriteDao): FavoriteLocalDataSource {

    override val favoriteWaifus: Flow<List<FavoriteItem>> = favDao.getAllFavs().map { it.toDomainModel() }

    override suspend fun isFavEmpty(): Boolean = favDao.favoriteCount() == 0

    override fun findFavById(id: Int): Flow<FavoriteItem> = favDao.findFavById(id).map { it.toDomainModel() }

    override suspend fun updateIm(waifu: WaifuImItem): Error? = tryCall {
        favDao.updateFav(waifu.fromImDomainModel())
    }.fold(ifLeft = { it }, ifRight = { null })

    override suspend fun updatePic(waifu: WaifuImItem): Error? = tryCall {
        favDao.updateFav(waifu.fromImDomainModel())
    }.fold(ifLeft = { it }, ifRight = { null })

    override suspend fun saveIm(waifu: WaifuImItem): Error? = tryCall {
        favDao.insertFavorite(waifu.fromImDomainModel())
    }.fold(ifLeft = { it }, ifRight = { null })

    override suspend fun savePic(waifu: WaifuPicItem): Error? = tryCall {
        favDao.insertFavorite(waifu.fromPicDomainModel())
    }.fold(ifLeft = { it }, ifRight = { null })

    override suspend fun saveBest(waifu: WaifuBestItem): Error? = tryCall {
        favDao.insertFavorite(waifu.fromBestDomainModel())
    }.fold(ifLeft = { it }, ifRight = { null })

    override suspend fun save(waifu: FavoriteItem): Error? = tryCall {
        favDao.insertFavorite(waifu.fromDomainModel())
    }.fold(ifLeft = { it }, ifRight = { null })

    override suspend fun deleteIm(waifu: WaifuImItem): Error? = tryCall {
        favDao.deleteFav(waifu.fromImDomainModel())
    }.fold(ifLeft = { it }, ifRight = { null })

    override suspend fun deletePic(waifu: WaifuPicItem): Error? = tryCall {
        favDao.deleteFav(waifu.fromPicDomainModel())
    }.fold(ifLeft = { it }, ifRight = { null })

    override suspend fun delete(waifu: FavoriteItem): Error? = tryCall {
        favDao.deleteFav(waifu.fromDomainModel())
    }.fold(ifLeft = { it }, ifRight = { null })

    override suspend fun deleteAll(): Error? = tryCall {
        favDao.deleteAll()
    }.fold(ifLeft = { it }, ifRight = { null })

}

private fun List<FavoriteDbItem>.toDomainModel(): List<FavoriteItem> = map { it.toDomainModel() }

private fun FavoriteDbItem.toDomainModel(): FavoriteItem =
    FavoriteItem(
        id,
        url,
        title,
        isFavorite
    )

private fun WaifuPicItem.fromPicDomainModel(): FavoriteDbItem =
    FavoriteDbItem(
        id = 0,
        url,
        title = url.substringAfterLast('/').substringBeforeLast('.'),
        isFavorite
    )

private fun WaifuImItem.fromImDomainModel(): FavoriteDbItem =
    FavoriteDbItem(
        id = 0,
        url,
        title = imageId.toString(),
        isFavorite
    )

private fun WaifuBestItem.fromBestDomainModel(): FavoriteDbItem =
    FavoriteDbItem(
        id = 0,
        url,
        title = artistName + animeName,
        isFavorite
    )

private fun FavoriteItem.fromDomainModel(): FavoriteDbItem =
    FavoriteDbItem(
        id,
        url,
        title,
        isFavorite
    )