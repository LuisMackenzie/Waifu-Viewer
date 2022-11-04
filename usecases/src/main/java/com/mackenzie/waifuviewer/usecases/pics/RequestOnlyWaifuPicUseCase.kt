package com.mackenzie.waifuviewer.usecases.pics

import com.mackenzie.waifuviewer.data.WaifusPicRepository
import javax.inject.Inject

class RequestOnlyWaifuPicUseCase @Inject constructor(private val repo: WaifusPicRepository){

    suspend operator fun invoke() = repo.requestOnlyWaifuPic()

}