package com.mackenzie.waifuviewer.ui.fakes

import com.mackenzie.waifuviewer.data.db.dao.WaifuPicDao
import com.mackenzie.waifuviewer.data.db.WaifuPicDbItem
import com.mackenzie.waifuviewer.data.server.models.WaifuPic
import com.mackenzie.waifuviewer.data.server.WaifuPicService
import com.mackenzie.waifuviewer.data.server.models.WaifuPicsRequest
import com.mackenzie.waifuviewer.data.server.models.WaifuPicsResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import retrofit2.Response

class FakeWaifuPicDao(waifusPic: List<WaifuPicDbItem> = emptyList()) : WaifuPicDao {

    private val inMemoryWaifus = MutableStateFlow(waifusPic)

    private lateinit var findWaifuFlow: MutableStateFlow<WaifuPicDbItem>

    override fun getAllPic(): Flow<List<WaifuPicDbItem>> = inMemoryWaifus

    override fun findPicsById(id: Int): Flow<WaifuPicDbItem> {
        findWaifuFlow = MutableStateFlow(inMemoryWaifus.value.first { it.id == id })
        return findWaifuFlow
    }

    override suspend fun waifuPicsCount(): Int = inMemoryWaifus.value.size

    override suspend fun insertWaifuPics(waifu: WaifuPicDbItem) {
        inMemoryWaifus.update { it }
    }

    override suspend fun insertAllWaifuPics(waifus: List<WaifuPicDbItem>) {
        inMemoryWaifus.value = waifus

        if (::findWaifuFlow.isInitialized) {
            waifus.firstOrNull() { it.id == findWaifuFlow.value.id }
                ?.let { findWaifuFlow.value = it }
        }

    }

    override suspend fun updateWaifuPics(waifu: WaifuPicDbItem) {
        inMemoryWaifus.update { it }
    }

    override suspend fun deletePics(waifu: WaifuPicDbItem) {
        inMemoryWaifus.update { it }
    }

    override suspend fun deleteAll() {
        inMemoryWaifus.update { it }
    }

}

class FakeRemotePicsService(private val waifus: List<String> = emptyList()) : WaifuPicService {

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