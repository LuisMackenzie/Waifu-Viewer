package com.mackenzie.waifuviewer.data.db.datasources

import com.mackenzie.waifuviewer.data.db.FavoriteDao
import com.mackenzie.waifuviewer.data.db.WaifuImDao
import javax.inject.Inject

class RoomBestDataSource @Inject constructor(
    private val imDao: WaifuImDao,
    private val favDao: FavoriteDao) {

}