package com.mackenzie.waifuviewer.data.db.datasources

import com.mackenzie.waifuviewer.data.datasource.WaifusBestGifLocalDataSource
import com.mackenzie.waifuviewer.data.db.WaifuBestDao
import com.mackenzie.waifuviewer.data.db.WaifuBestGifDbItem
import com.mackenzie.waifuviewer.data.tryCall
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.WaifuBestItemGif
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoomGifDataSource @Inject constructor(private val bestDao: WaifuBestDao) : WaifusBestGifLocalDataSource {

    override val waifusGif: Flow<List<WaifuBestItemGif>> = bestDao.getAllGif().map { it.toDomainModel() }

    override suspend fun isGifEmpty(): Boolean = bestDao.waifuGifCount() == 0

    override fun findGifById(id: Int): Flow<WaifuBestItemGif> = bestDao.findGifById(id).map { it.toDomainModel() }

    override suspend fun saveGif(waifus: List<WaifuBestItemGif>): Error? = tryCall {
        bestDao.insertAllWaifuGif(waifus.fromDomainModel())
    }.fold(ifLeft = { it }, ifRight = { null })

    override suspend fun saveOnlyGif(waifu: WaifuBestItemGif): Error? = tryCall {
        bestDao.updateWaifuGif(waifu.fromDomainModel())
    }.fold(ifLeft = { it }, ifRight = { null })

    override suspend fun deleteAllGif(): Error? = tryCall {
        bestDao.deleteAllGif()
    }.fold(ifLeft = { it }, ifRight = { null })

}

private fun List<WaifuBestGifDbItem>.toDomainModel(): List<WaifuBestItemGif> = map { it.toDomainModel() }

private fun WaifuBestGifDbItem.toDomainModel(): WaifuBestItemGif =
    WaifuBestItemGif(
        id,
        animeName,
        url,
        isFavorite
    )

private fun List<WaifuBestItemGif>.fromDomainModel(): List<WaifuBestGifDbItem> = map { it.fromDomainModel() }

private fun WaifuBestItemGif.fromDomainModel(): WaifuBestGifDbItem =
    WaifuBestGifDbItem(
        id,
        url,
        animeName,
        isFavorite
    )