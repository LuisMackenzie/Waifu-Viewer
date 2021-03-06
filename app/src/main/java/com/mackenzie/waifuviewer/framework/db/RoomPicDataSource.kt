package com.mackenzie.waifuviewer.framework.db

import com.mackenzie.waifuviewer.data.datasource.WaifusPicLocalDataSource
import com.mackenzie.waifuviewer.domain.WaifuPicItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class RoomPicDataSource(private val PicDao: WaifuPicDao) : WaifusPicLocalDataSource {

    override val waifusPic: Flow<List<WaifuPicItem>> = PicDao.getAllPic().map { it.reversed().toDomainModel() }

    override suspend fun isPicsEmpty(): Boolean = withContext(Dispatchers.IO) { PicDao.waifuPicsCount() == 0 }

    override fun findPicById(id: Int): Flow<WaifuPicItem> = PicDao.findPicsById(id).map { it.toDomainModel() }

    override suspend fun savePics(waifus: List<WaifuPicItem>)= withContext(Dispatchers.IO) {
        PicDao.insertAllWaifuPics(waifus.fromDomainModel())
    }

}

private fun List<WaifuPicDbItem>.toDomainModel(): List<WaifuPicItem> = map { it.toDomainModel() }

private fun WaifuPicDbItem.toDomainModel(): WaifuPicItem = WaifuPicItem(
    id,
    url,
    isFavorite
)

private fun List<WaifuPicItem>.fromDomainModel(): List<WaifuPicDbItem> = map { it.fromDomainModel() }

private fun WaifuPicItem.fromDomainModel(): WaifuPicDbItem = WaifuPicDbItem(
    id,
    url,
    isFavorite
)