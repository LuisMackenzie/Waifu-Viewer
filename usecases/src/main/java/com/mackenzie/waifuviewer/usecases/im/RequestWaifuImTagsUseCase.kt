package com.mackenzie.waifuviewer.usecases.im

import com.mackenzie.waifuviewer.data.WaifusImRepository
import com.mackenzie.waifuviewer.domain.Error
import javax.inject.Inject

class RequestWaifuImTagsUseCase @Inject constructor(private val repo: WaifusImRepository) {

    suspend operator fun invoke(): Error? {
        return repo.requestWaifuImTags()
    }

}