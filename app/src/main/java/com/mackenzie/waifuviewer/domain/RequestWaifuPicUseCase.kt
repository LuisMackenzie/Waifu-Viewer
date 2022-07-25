package com.mackenzie.waifuviewer.domain

import com.mackenzie.waifuviewer.data.Error
import com.mackenzie.waifuviewer.data.datasource.WaifusPicRepository

class RequestWaifuPicUseCase(private val repo: WaifusPicRepository) {

    suspend operator fun invoke(isNsfw:String, tag:String) : Error?
    = repo.requestWaifusPics(isNsfw, tag)

}