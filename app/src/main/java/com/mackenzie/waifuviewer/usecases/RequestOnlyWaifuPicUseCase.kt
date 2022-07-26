package com.mackenzie.waifuviewer.usecases

import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.data.datasource.WaifusPicRepository

class RequestOnlyWaifuPicUseCase (private val repo: WaifusPicRepository){

    suspend operator fun invoke() : Error? = repo.requestOnlyWaifuPicFix()

    suspend fun get()  = repo.requestOnlyWaifuPic()

}