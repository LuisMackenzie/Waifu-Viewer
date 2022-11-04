package com.mackenzie.waifuviewer.usecases.pics

import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.data.WaifusPicRepository
import javax.inject.Inject

class RequestMoreWaifuPicUseCase @Inject constructor(private val repo: WaifusPicRepository) {

    suspend operator fun invoke(isNsfw:String, tag:String): Error? {
        return repo.requestNewWaifusPics(isNsfw, tag)
    }

}