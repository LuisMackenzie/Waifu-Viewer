package com.mackenzie.waifuviewer.data

import com.mackenzie.waifuviewer.data.datasource.FavoriteLocalDataSource
import com.mackenzie.waifuviewer.data.datasource.WaifusBestGifLocalDataSource
import com.mackenzie.waifuviewer.data.datasource.WaifusBestPngLocalDataSource
import com.mackenzie.waifuviewer.data.datasource.WaifusBestRemoteDataSource
import javax.inject.Inject

class WaifusBestRepository @Inject constructor(
    private val localPngDataSource: WaifusBestPngLocalDataSource,
    private val localGifDataSource: WaifusBestGifLocalDataSource,
    private val favDataSource: FavoriteLocalDataSource,
    private val remotePngDataSource: WaifusBestRemoteDataSource
) {
}