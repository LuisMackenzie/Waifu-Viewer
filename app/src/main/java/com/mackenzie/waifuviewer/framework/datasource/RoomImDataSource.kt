package com.mackenzie.waifuviewer.framework.datasource

import com.mackenzie.waifuviewer.data.datasource.WaifusImLocalDataSource
import com.mackenzie.waifuviewer.framework.db.WaifuImDao
import com.mackenzie.waifuviewer.framework.db.WaifuImDbItem
import com.mackenzie.waifuviewer.domain.WaifuImItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class RoomImDataSource(private val ImDao: WaifuImDao) : WaifusImLocalDataSource {

    override val waifusIm: Flow<List<WaifuImItem>> = ImDao.getAllIm().map { it.toDomainModel()}

    override suspend fun isImEmpty(): Boolean =  withContext(Dispatchers.IO) { ImDao.waifuImCount() == 0 }

    override fun findImById(id: Int): Flow<WaifuImItem> = ImDao.findImById(id).map { it.toDomainModel() }

    override suspend fun saveIm(waifus: List<WaifuImItem>) = withContext(Dispatchers.IO){
        ImDao.insertAllWaifuIm(waifus.fromDomainModel())
    }

}

private fun List<WaifuImDbItem>.toDomainModel(): List<WaifuImItem> = map { it.toDomainModel() }

private fun WaifuImDbItem.toDomainModel(): WaifuImItem = WaifuImItem(
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