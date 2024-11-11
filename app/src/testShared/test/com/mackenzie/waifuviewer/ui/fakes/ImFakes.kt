package com.mackenzie.waifuviewer.ui.fakes

import com.mackenzie.waifuviewer.data.db.WaifuImDao
import com.mackenzie.waifuviewer.data.db.WaifuImDbItem
import com.mackenzie.waifuviewer.data.server.models.AnimeResult
import com.mackenzie.waifuviewer.data.server.models.TraceMoeResult
import com.mackenzie.waifuviewer.data.server.models.WaifuIm
import com.mackenzie.waifuviewer.data.server.WaifuImService
import com.mackenzie.waifuviewer.data.server.models.WaifuImResult
import com.mackenzie.waifuviewer.data.server.WaifuTraceMoeService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FakeWaifuImDao(waifusIm: List<WaifuImDbItem> = emptyList()) : WaifuImDao {

    private val inMemoryWaifus = MutableStateFlow(waifusIm)

    private lateinit var findWaifuFlow: MutableStateFlow<WaifuImDbItem>

    override fun getAllIm(): Flow<List<WaifuImDbItem>> = inMemoryWaifus

    override fun findImById(id: Int): Flow<WaifuImDbItem> {
        findWaifuFlow = MutableStateFlow(inMemoryWaifus.value.first { it.id == id })
        return findWaifuFlow
    }

    override suspend fun waifuImCount(): Int = inMemoryWaifus.value.size

    override suspend fun insertWaifuIm(waifu: WaifuImDbItem) {
        inMemoryWaifus.update { it }
    }

    override suspend fun insertAllWaifuIm(waifus: List<WaifuImDbItem>) {
        inMemoryWaifus.value = waifus

        if (::findWaifuFlow.isInitialized) {
            waifus.firstOrNull() { it.id == findWaifuFlow.value.id }
                ?.let { findWaifuFlow.value = it }
        }

    }

    override suspend fun updateWaifuIm(waifu: WaifuImDbItem) {
        inMemoryWaifus.update { it }
    }

    override suspend fun deleteIm(waifu: WaifuImDbItem) {
        inMemoryWaifus.update { it }
    }

    override suspend fun deleteAll() {
        inMemoryWaifus.update { it }
    }

}

class FakeRemoteImService(private val waifus: List<WaifuIm> = emptyList()) : WaifuImService {

    override suspend fun getRandomWaifuIm(
        isNsfw: Boolean,
        tags: String,
        isGif: Boolean,
        orientation: String,
        manyWaifus: Int
    ) = WaifuImResult(waifus)

    override suspend fun getOnlyRandomWaifuIm(
        isNsfw: Boolean,
        tags: String,
        isGif: Boolean,
        orientation: String
    ) = WaifuImResult(waifus)
}

class FakeRemoteMoeService(private val waifus: List<AnimeResult> = emptyList()) : WaifuTraceMoeService {

    override suspend fun searchAnime(imageUrl: String) = TraceMoeResult(
        frameCount = 0,
        error = null,
        result = waifus
    )

}