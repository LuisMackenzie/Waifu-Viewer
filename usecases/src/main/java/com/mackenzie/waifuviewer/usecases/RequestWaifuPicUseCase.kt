package com.mackenzie.waifuviewer.usecases

import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.data.WaifusPicRepository
import javax.inject.Inject

class RequestWaifuPicUseCase @Inject constructor(private val repo: WaifusPicRepository) {

    suspend operator fun invoke(isNsfw:String, tag:String): Error? {
        return repo.requestWaifusPics(isNsfw, tag)
    }

}