package com.mackenzie.waifuviewer.usecases

import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.data.datasource.WaifusPicRepository

class RequestMoreWaifuPicUseCase(private val repo: WaifusPicRepository) {

    suspend operator fun invoke(isNsfw:String, tag:String) : Error?
            = repo.requestNewWaifusPics(isNsfw, tag)

}