package com.mackenzie.waifuviewer.data.server

import arrow.core.Either
import com.mackenzie.waifuviewer.data.datasource.WaifusMoeRemoteDataSource
import com.mackenzie.waifuviewer.data.server.models.AnimeResult
import com.mackenzie.waifuviewer.data.server.models.RemoteConnect
import com.mackenzie.waifuviewer.data.tryCall
import com.mackenzie.waifuviewer.domain.AnimeSearchItem
import com.mackenzie.waifuviewer.domain.Error
import javax.inject.Inject

class ServerMoeDataSource @Inject constructor(private val remoteService: RemoteConnect): WaifusMoeRemoteDataSource {

    override suspend fun getMoeWaifuSearch(imageUrl: String): Either<Error, List<AnimeSearchItem>> = tryCall {
        remoteService.serviceMoe
            .searchAnime(imageUrl)
            .result
            .toDomainModel()
    }
}

private fun List<AnimeResult>.toDomainModel(): List<AnimeSearchItem> = map { it.toDomainModel() }

private fun AnimeResult.toDomainModel(): AnimeSearchItem =
    AnimeSearchItem(
        0,
        anilist,
        filename,
        episode ?: 0,
        from ?: 0f,
        to,
        similarity,
        video,
        image
    )