package com.mackenzie.waifuviewer.data

import com.mackenzie.waifuviewer.data.datasource.VideoHubRemoteDataSource
import com.mackenzie.waifuviewer.domain.Error
import javax.inject.Inject

class VideoHubRepository @Inject constructor(val remoteDataSource: VideoHubRemoteDataSource) {

    suspend fun requestVideoSources(): Error? {
        val error = remoteDataSource.getVideoServer()
            .fold(ifLeft = { it }) { null }

        // Guardar en DBBD
        if (error != null) return error else return null
    }


}