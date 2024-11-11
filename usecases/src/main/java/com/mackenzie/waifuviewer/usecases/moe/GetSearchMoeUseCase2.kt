package com.mackenzie.waifuviewer.usecases.moe

import com.mackenzie.waifuviewer.data.TraceMoeRepository
import com.mackenzie.waifuviewer.domain.AnimeSearchItem
import javax.inject.Inject

class GetSearchMoeUseCase2 @Inject constructor(private val repo: TraceMoeRepository) {

    suspend operator fun invoke(imageUrl: String) = repo.getMoeWaifuSearch2(imageUrl)

}