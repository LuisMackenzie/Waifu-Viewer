package com.mackenzie.waifuviewer.data.db

import com.mackenzie.waifuviewer.data.datasource.WaifusPicLocalDataSource
import com.mackenzie.waifuviewer.data.tryCall
import com.mackenzie.waifuviewer.domain.WaifuPicItem
import com.mackenzie.waifuviewer.domain.Error
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RoomPicDataSource @Inject constructor(private val PicDao: WaifuPicDao) : WaifusPicLocalDataSource {

    override val waifusPic: Flow<List<WaifuPicItem>> = PicDao.getAllPic().map { it.toDomainModel() }

    override suspend fun isPicsEmpty(): Boolean = PicDao.waifuPicsCount() == 0

    override fun findPicById(id: Int): Flow<WaifuPicItem> = PicDao.findPicsById(id).map { it.toDomainModel() }

    override suspend fun savePics(waifus: List<WaifuPicItem>): Error? = tryCall {
        PicDao.insertAllWaifuPics(waifus.fromDomainModel())
    }.fold(ifLeft = { it }, ifRight = { null })

    override suspend fun saveOnlyPics(waifu: WaifuPicItem): Error? = tryCall {
        PicDao.insertWaifuPics(waifu.fromDomainModel())
    }.fold(ifLeft = { it }, ifRight = { null })

}

private fun List<WaifuPicDbItem>.toDomainModel(): List<WaifuPicItem> = map { it.toDomainModel() }

private fun WaifuPicDbItem.toDomainModel(): WaifuPicItem =
    WaifuPicItem(
        id,
        url,
        isFavorite
    )

private fun List<WaifuPicItem>.fromDomainModel(): List<WaifuPicDbItem> = map { it.fromDomainModel() }

private fun WaifuPicItem.fromDomainModel(): WaifuPicDbItem =
    WaifuPicDbItem(
    id,
    url,
    isFavorite
)