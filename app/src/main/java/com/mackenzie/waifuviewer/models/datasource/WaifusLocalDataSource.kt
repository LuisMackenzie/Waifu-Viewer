package com.mackenzie.waifuviewer.models.datasource

import com.mackenzie.waifuviewer.models.db.WaifuImDao
import com.mackenzie.waifuviewer.models.db.WaifuImItem
import com.mackenzie.waifuviewer.models.db.WaifuPicDao
import com.mackenzie.waifuviewer.models.db.WaifuPicItem
import kotlinx.coroutines.flow.Flow

class WaifusLocalDataSource(private val PicDao: WaifuPicDao, private val ImDao: WaifuImDao) {

    val waifusPic: Flow<List<WaifuPicItem>> = PicDao.getAllPic()
    val waifusIm: Flow<List<WaifuImItem>> = ImDao.getAllIm()

    fun isPicsEmpty(): Boolean = PicDao.waifuCount() == 0
    fun isImEmpty(): Boolean = ImDao.waifuCount() == 0

    fun findPicById(id: Int): Flow<WaifuPicItem> = PicDao.findById(id)
    fun findImById(id: Int): Flow<WaifuImItem> = ImDao.findById(id)

    fun savePics(waifus: List<WaifuPicItem>) {
        PicDao.insertAllWaifus(waifus)
    }
    fun saveIm(waifus: List<WaifuImItem>) {
        ImDao.insertAllWaifus(waifus)
    }

}