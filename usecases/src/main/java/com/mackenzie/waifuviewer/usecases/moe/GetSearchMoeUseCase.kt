package com.mackenzie.waifuviewer.usecases.moe

import arrow.core.Either
import com.mackenzie.waifuviewer.data.TraceMoeRepository
import com.mackenzie.waifuviewer.domain.AnimeSearchItem
import com.mackenzie.waifuviewer.domain.Error
import javax.inject.Inject

class GetSearchMoeUseCase @Inject constructor(private val repo: TraceMoeRepository) {

    suspend operator fun invoke(imageUrl: String): Either<Error, List<AnimeSearchItem>> {
        return repo.getMoeWaifuSearch(imageUrl)
    }
}