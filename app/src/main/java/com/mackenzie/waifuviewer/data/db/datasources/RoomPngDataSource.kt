package com.mackenzie.waifuviewer.data.db.datasources

import com.mackenzie.waifuviewer.data.datasource.WaifusBestPngLocalDataSource
import com.mackenzie.waifuviewer.data.db.WaifuBestDao
import com.mackenzie.waifuviewer.data.db.WaifuBestPngDbItem
import com.mackenzie.waifuviewer.data.tryCall
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.WaifuBestItemPng
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoomPngDataSource @Inject constructor(private val bestDao: WaifuBestDao) : WaifusBestPngLocalDataSource  {

    override val waifusPng: Flow<List<WaifuBestItemPng>> = bestDao.getAllPng().map { it.toDomainModel() }

    override suspend fun isPngEmpty(): Boolean = bestDao.waifuPngCount() == 0

    override fun findPngById(id: Int): Flow<WaifuBestItemPng> = bestDao.findPngById(id).map { it.toDomainModel() }

    override suspend fun savePng(waifus: List<WaifuBestItemPng>): Error? = tryCall {
        bestDao.insertAllWaifuPng(waifus.fromDomainModel())
    }.fold(ifLeft = { it }, ifRight = { null })

    override suspend fun saveOnlyPng(waifu: WaifuBestItemPng): Error? = tryCall {
        bestDao.updateWaifuPng(waifu.fromDomainModel())
    }.fold(ifLeft = { it }, ifRight = { null })

    override suspend fun deleteAllPng(): Error? = tryCall {
        bestDao.deleteAllPng()
    }.fold(ifLeft = { it }, ifRight = { null })

}

private fun List<WaifuBestPngDbItem>.toDomainModel(): List<WaifuBestItemPng> = map { it.toDomainModel() }

private fun WaifuBestPngDbItem.toDomainModel(): WaifuBestItemPng =
    WaifuBestItemPng(
        id,
        artistHref,
        artistName,
        sourceUrl,
        url,
        isFavorite
    )

private fun List<WaifuBestItemPng>.fromDomainModel(): List<WaifuBestPngDbItem> = map { it.fromDomainModel() }

private fun WaifuBestItemPng.fromDomainModel(): WaifuBestPngDbItem =
    WaifuBestPngDbItem(
        id,
        artistHref,
        artistName,
        sourceUrl,
        url,
        isFavorite
    )