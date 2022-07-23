package com.mackenzie.waifuviewer.models.datasource

import com.mackenzie.waifuviewer.models.db.WaifuImDao
import com.mackenzie.waifuviewer.models.db.WaifuImItem
import com.mackenzie.waifuviewer.models.db.WaifuPicDao
import com.mackenzie.waifuviewer.models.db.WaifuPicItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class WaifusPicLocalDataSource(private val PicDao: WaifuPicDao) {

    val waifusPic: Flow<List<WaifuPicItem>> = PicDao.getAllPic()

    suspend fun isPicsEmpty(): Boolean = withContext(Dispatchers.IO) { PicDao.waifuPicsCount() == 0 }

    fun findPicById(id: Int): Flow<WaifuPicItem> = PicDao.findPicsById(id)

    suspend fun savePics(waifus: List<WaifuPicItem>)= withContext(Dispatchers.IO) {
        PicDao.insertAllWaifuPics(waifus)
    }

}

class WaifusImLocalDataSource(private val ImDao: WaifuImDao) {

    val waifusIm: Flow<List<WaifuImItem>> = ImDao.getAllIm()

    suspend fun isImEmpty(): Boolean =  withContext(Dispatchers.IO) { ImDao.waifuImCount() == 0 }

    fun findImById(id: Int): Flow<WaifuImItem> = ImDao.findImById(id)

    suspend fun saveIm(waifus: List<WaifuImItem>) = withContext(Dispatchers.IO){
        ImDao.insertAllWaifuIm(waifus)
    }

}