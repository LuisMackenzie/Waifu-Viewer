package com.mackenzie.waifuviewer.data.datasource

import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.video.VideoDomainItem
import com.mackenzie.waifuviewer.domain.video.VideoListItem
import kotlinx.coroutines.flow.Flow

interface VideoHubLocalDataSource {

    val videoList: Flow<List<VideoListItem>>
    suspend fun isEmpty(): Boolean
    fun findById(id: Int): Flow<VideoDomainItem>
    suspend fun saveList(videos: List<VideoListItem>): Error?
    suspend fun saveOnly(video: VideoListItem): Error?
    suspend fun deleteAll(): Error?

}