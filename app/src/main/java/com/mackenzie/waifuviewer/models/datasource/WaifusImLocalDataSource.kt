package com.mackenzie.waifuviewer.models.datasource

import com.mackenzie.waifuviewer.models.db.WaifuImDao
import com.mackenzie.waifuviewer.models.db.WaifuImItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class WaifusImLocalDataSource(private val ImDao: WaifuImDao) {

    val waifusIm: Flow<List<WaifuImItem>> = ImDao.getAllIm()

    suspend fun isImEmpty(): Boolean =  withContext(Dispatchers.IO) { ImDao.waifuImCount() == 0 }

    fun findImById(id: Int): Flow<WaifuImItem> = ImDao.findImById(id)

    suspend fun saveIm(waifus: List<WaifuImItem>) = withContext(Dispatchers.IO){
        ImDao.insertAllWaifuIm(waifus)
    }

}