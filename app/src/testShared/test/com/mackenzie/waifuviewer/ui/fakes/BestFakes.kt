package com.mackenzie.waifuviewer.ui.fakes

import com.mackenzie.waifuviewer.data.db.WaifuBestDao
import com.mackenzie.waifuviewer.data.db.WaifuBestDbItem
import com.mackenzie.waifuviewer.data.server.*
import com.mackenzie.waifuviewer.data.server.models.WaifuBestGif
import com.mackenzie.waifuviewer.data.server.models.WaifuBestGifResult
import com.mackenzie.waifuviewer.data.server.models.WaifuBestPng
import com.mackenzie.waifuviewer.data.server.models.WaifuBestPngResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FakeWaifuBestDao(waifusBest: List<WaifuBestDbItem> = emptyList()): WaifuBestDao {

    private val inMemoryWaifus = MutableStateFlow(waifusBest)

    private lateinit var findWaifuFlow: MutableStateFlow<WaifuBestDbItem>

    override fun getAll(): Flow<List<WaifuBestDbItem>> = inMemoryWaifus

    override fun findById(id: Int): Flow<WaifuBestDbItem> {
        findWaifuFlow = MutableStateFlow(inMemoryWaifus.value.first { it.id == id })
        return findWaifuFlow
    }

    override suspend fun waifuCount(): Int = inMemoryWaifus.value.size

    override suspend fun insertWaifu(waifu: WaifuBestDbItem) {
        inMemoryWaifus.update { it }
    }

    override suspend fun insertAllWaifu(waifus: List<WaifuBestDbItem>) {
        inMemoryWaifus.value = waifus

        if (::findWaifuFlow.isInitialized) {
            waifus.firstOrNull() { it.id == findWaifuFlow.value.id }
                ?.let { findWaifuFlow.value = it }
        }
    }

    override suspend fun updateWaifu(waifu: WaifuBestDbItem) {
        inMemoryWaifus.update { it }
    }

    override suspend fun delete(waifu: WaifuBestDbItem) {
        inMemoryWaifus.update { it }
    }

    override suspend fun deleteAll() {
        inMemoryWaifus.update { it }
    }

}

class FakeRemoteBestService (
    private val waifus: List<WaifuBestPng> = emptyList(),
    private val waifusGif: List<WaifuBestGif> = emptyList()) : WaifuBestService {

    override suspend fun getRandomWaifuBestPng(tags: String, amount: Int) = WaifuBestPngResult(waifus)

    override suspend fun getRandomWaifuBestGif(tags: String, amount: Int) = WaifuBestGifResult(waifusGif)

    override suspend fun getOnlyRandomWaifuBestPng(tags: String, amount: Int) = WaifuBestPngResult(waifus)

    override suspend fun getOnlyRandomWaifuBestGif(tags: String, amount: Int)  = WaifuBestGifResult(waifusGif)


}