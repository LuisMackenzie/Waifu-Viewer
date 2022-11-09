package com.mackenzie.waifuviewer.ui.fakes

import com.mackenzie.waifuviewer.data.db.WaifuPicDao
import com.mackenzie.waifuviewer.data.db.WaifuPicDbItem
import com.mackenzie.waifuviewer.data.server.WaifuPic
import com.mackenzie.waifuviewer.data.server.WaifuPicService
import com.mackenzie.waifuviewer.data.server.WaifuPicsRequest
import com.mackenzie.waifuviewer.data.server.WaifuPicsResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import retrofit2.Response

class FakeWaifuPicDao(waifusPic: List<WaifuPicDbItem> = emptyList()) : WaifuPicDao {

    private val inMemoryMovies = MutableStateFlow(waifusPic)

    private lateinit var findWaifuFlow: MutableStateFlow<WaifuPicDbItem>

    override fun getAllPic(): Flow<List<WaifuPicDbItem>> = inMemoryMovies

    override fun findPicsById(id: Int): Flow<WaifuPicDbItem> {
        findWaifuFlow = MutableStateFlow(inMemoryMovies.value.first { it.id == id })
        return findWaifuFlow
    }

    override suspend fun waifuPicsCount(): Int = inMemoryMovies.value.size

    override suspend fun insertWaifuPics(waifu: WaifuPicDbItem) {
        inMemoryMovies.update { it }
    }

    override suspend fun insertAllWaifuPics(waifus: List<WaifuPicDbItem>) {
        inMemoryMovies.value = waifus

        if (::findWaifuFlow.isInitialized) {
            waifus.firstOrNull() { it.id == findWaifuFlow.value.id }
                ?.let { findWaifuFlow.value = it }
        }

    }

    override suspend fun updateWaifuPics(waifu: WaifuPicDbItem) {
        inMemoryMovies.update { it }
    }

    override suspend fun deletePics(waifu: WaifuPicDbItem) {
        inMemoryMovies.update { it }
    }

    override suspend fun deleteAll() {
        inMemoryMovies.update { it }
    }

}

class FakeRemotePicsService(private val waifus: List<String>) : WaifuPicService {

    override suspend fun getRandomWaifuPics(
        many: String,
        type: String,
        category: String,
        body: WaifuPicsRequest
    ): Response <WaifuPicsResult> {
        val waifuPicsResult = WaifuPicsResult(waifus)
        return Response.success(waifuPicsResult)
    }

    override suspend fun getOnlyWaifuPic(type: String, category: String) = WaifuPic(waifus[0])

}