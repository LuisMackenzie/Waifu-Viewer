package com.mackenzie.waifuviewer.usecases.video

import com.mackenzie.waifuviewer.data.VideoHubRepository
import com.mackenzie.waifuviewer.domain.Error
import javax.inject.Inject

class GetVideoServersUseCase @Inject constructor(private val repo: VideoHubRepository) {

    suspend operator fun invoke(apiKey: String): Error? = repo.requestVideoSources()

}