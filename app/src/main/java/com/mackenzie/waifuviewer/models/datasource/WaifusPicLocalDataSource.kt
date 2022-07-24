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