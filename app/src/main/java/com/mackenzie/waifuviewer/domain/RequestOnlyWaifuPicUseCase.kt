package com.mackenzie.waifuviewer.domain

import com.mackenzie.waifuviewer.data.Error
import com.mackenzie.waifuviewer.data.datasource.WaifusPicRepository

class RequestOnlyWaifuPicUseCase (private val repo: WaifusPicRepository){

    suspend operator fun invoke() : Error? = repo.requestOnlyWaifuPicFix()

    suspend fun get()  = repo.requestOnlyWaifuPic()

}