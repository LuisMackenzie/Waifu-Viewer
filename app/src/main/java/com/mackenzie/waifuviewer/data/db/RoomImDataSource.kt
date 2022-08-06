package com.mackenzie.waifuviewer.data.db

import com.mackenzie.waifuviewer.data.datasource.WaifusImLocalDataSource
import com.mackenzie.waifuviewer.data.tryCall
import com.mackenzie.waifuviewer.data.trySave
import com.mackenzie.waifuviewer.domain.WaifuImItem
import com.mackenzie.waifuviewer.domain.Error
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class RoomImDataSource(private val ImDao: WaifuImDao) : WaifusImLocalDataSource {

    override val waifusIm: Flow<List<WaifuImItem>> = ImDao.getAllIm().map { it.toDomainModel()}

    override suspend fun isImEmpty(): Boolean = withContext(Dispatchers.IO) { ImDao.waifuImCount() == 0 }

    override fun findImById(id: Int): Flow<WaifuImItem> = ImDao.findImById(id).map { it.toDomainModel() }

    /*override suspend fun saveIm(waifus: List<WaifuImItem>): Error? = trySave {
        ImDao.insertAllWaifuIm(waifus.fromDomainModel())
    }

    override suspend fun saveOnlyIm(waifu: WaifuImItem): Error? = trySave {
        ImDao.insertWaifuIm(waifu.fromDomainModel())
    }*/

    override suspend fun saveIm(waifus: List<WaifuImItem>) = tryCall {
        ImDao.insertAllWaifuIm(waifus.fromDomainModel())
    }.fold(ifLeft = { it }, ifRight = { null })

    override suspend fun saveOnlyIm(waifu: WaifuImItem): Error? = tryCall {
        ImDao.insertWaifuIm(waifu.fromDomainModel())
    }.fold(ifLeft = { it }, ifRight = { null })

}

private fun List<WaifuImDbItem>.toDomainModel(): List<WaifuImItem> = map { it.toDomainModel() }

private fun WaifuImDbItem.toDomainModel(): WaifuImItem =
    WaifuImItem(
        id,
        dominant_color,
        file,
        height,
        imageId,
        isNsfw,
        url,
        width,
        isFavorite
    )

private fun List<WaifuImItem>.fromDomainModel(): List<WaifuImDbItem> = map { it.fromDomainModel() }

private fun WaifuImItem.fromDomainModel(): WaifuImDbItem = WaifuImDbItem(
    id,
    dominant_color,
    file,
    height,
    imageId,
    isNsfw,
    url,
    width,
    isFavorite
)