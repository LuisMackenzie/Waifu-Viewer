package com.mackenzie.waifuviewer.data.db.datasources

import androidx.paging.*
import com.mackenzie.waifuviewer.data.datasource.WaifusImLocalDataSource
import com.mackenzie.waifuviewer.data.db.WaifuImDao
import com.mackenzie.waifuviewer.data.db.WaifuImDbItem
import com.mackenzie.waifuviewer.data.tryCall
import com.mackenzie.waifuviewer.domain.WaifuImItem
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.ui.common.Scope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class RoomImDataSource @Inject constructor(private val imDao: WaifuImDao) : WaifusImLocalDataSource {

    override val waifusIm: Flow<List<WaifuImItem>> = imDao.getAllIm().map { it.toDomainModel() }

    override val waifusImPaged: Flow<PagingData<WaifuImItem>> = Pager(
        config = PagingConfig(pageSize = 10, enablePlaceholders = true, maxSize = 200)) {
        imDao.getAllImPaged()
    }.flow.map {
        it.map { it.toDomainModel() }
    }

    override suspend fun isImEmpty(): Boolean = imDao.waifuImCount() == 0

    override fun findImById(id: Int): Flow<WaifuImItem> = imDao.findImById(id).map { it.toDomainModel() }

    override suspend fun saveIm(waifus: List<WaifuImItem>): Error? = tryCall {
        imDao.insertAllWaifuIm(waifus.fromDomainModel())
    }.fold(ifLeft = { it }, ifRight = { null })

    override suspend fun saveOnlyIm(waifu: WaifuImItem): Error? = tryCall {
        imDao.insertWaifuIm(waifu.fromDomainModel())
    }.fold(ifLeft = { it }, ifRight = { null })

    override suspend fun deleteAll(): Error? = tryCall {
        imDao.deleteAll()
    }.fold(ifLeft = { it }, ifRight = { null })

}

private fun List<WaifuImDbItem>.toDomainModel(): List<WaifuImItem> = map { it.toDomainModel() }

private fun WaifuImDbItem.toDomainModel(): WaifuImItem =
    WaifuImItem(
        id,
        dominantColor,
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
    dominantColor,
    file,
    height,
    imageId,
    isNsfw,
    url,
    width,
    isFavorite
)