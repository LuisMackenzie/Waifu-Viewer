package com.mackenzie.waifuviewer.ui.fakes

import com.mackenzie.waifuviewer.data.db.WaifuBestDao
import com.mackenzie.waifuviewer.data.db.WaifuBestDbItem
import com.mackenzie.waifuviewer.data.server.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FakeWaifuBestDao(waifusBest: List<WaifuBestDbItem> = emptyList()): WaifuBestDao {

    private val inMemoryMovies = MutableStateFlow(waifusBest)

    private lateinit var findWaifuFlow: MutableStateFlow<WaifuBestDbItem>

    override fun getAll(): Flow<List<WaifuBestDbItem>> = inMemoryMovies

    override fun findById(id: Int): Flow<WaifuBestDbItem> {
        findWaifuFlow = MutableStateFlow(inMemoryMovies.value.first { it.id == id })
        return findWaifuFlow
    }

    override suspend fun waifuCount(): Int = inMemoryMovies.value.size

    override suspend fun insertWaifu(waifu: WaifuBestDbItem) {
        inMemoryMovies.update { it }
    }

    override suspend fun insertAllWaifu(waifus: List<WaifuBestDbItem>) {
        inMemoryMovies.value = waifus

        if (::findWaifuFlow.isInitialized) {
            waifus.firstOrNull() { it.id == findWaifuFlow.value.id }
                ?.let { findWaifuFlow.value = it }
        }
    }

    override suspend fun updateWaifu(waifu: WaifuBestDbItem) {
        inMemoryMovies.update { it }
    }

    override suspend fun delete(waifu: WaifuBestDbItem) {
        inMemoryMovies.update { it }
    }

    override suspend fun deleteAll() {
        inMemoryMovies.update { it }
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