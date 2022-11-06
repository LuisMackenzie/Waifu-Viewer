package com.mackenzie.waifuviewer.data.db.datasources

import com.mackenzie.waifuviewer.data.datasource.WaifusBestLocalDataSource
import com.mackenzie.waifuviewer.data.db.WaifuBestDao
import com.mackenzie.waifuviewer.data.db.WaifuBestDbItem
import com.mackenzie.waifuviewer.data.tryCall
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.WaifuBestItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoomBestDataSource @Inject constructor(private val bestDao: WaifuBestDao) : WaifusBestLocalDataSource  {

    override val waifus: Flow<List<WaifuBestItem>> = bestDao.getAll().map { it.toDomainModel() }

    override suspend fun isEmpty(): Boolean = bestDao.waifuCount() == 0

    override fun findById(id: Int): Flow<WaifuBestItem> = bestDao.findById(id).map { it.toDomainModel() }

    override suspend fun save(waifus: List<WaifuBestItem>): Error? = tryCall {
        bestDao.insertAllWaifu(waifus.fromDomainModel())
    }.fold(ifLeft = { it }, ifRight = { null })

    override suspend fun saveOnly(waifu: WaifuBestItem): Error? = tryCall {
        bestDao.updateWaifu(waifu.fromDomainModel())
    }.fold(ifLeft = { it }, ifRight = { null })

    override suspend fun deleteAll(): Error? = tryCall {
        bestDao.deleteAll()
    }.fold(ifLeft = { it }, ifRight = { null })

}

private fun List<WaifuBestDbItem>.toDomainModel(): List<WaifuBestItem> = map { it.toDomainModel() }

private fun WaifuBestDbItem.toDomainModel(): WaifuBestItem =
    WaifuBestItem(
        id,
        artistHref,
        artistName,
        animeName,
        sourceUrl,
        url,
        isFavorite
    )

private fun List<WaifuBestItem>.fromDomainModel(): List<WaifuBestDbItem> = map { it.fromDomainModel() }

private fun WaifuBestItem.fromDomainModel(): WaifuBestDbItem =
    WaifuBestDbItem(
        id,
        artistHref,
        artistName,
        animeName,
        sourceUrl,
        url,
        isFavorite
    )