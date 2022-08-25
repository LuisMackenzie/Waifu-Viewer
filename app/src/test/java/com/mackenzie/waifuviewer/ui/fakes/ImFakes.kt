package com.mackenzie.waifuviewer.ui.fakes

import arrow.core.right
import com.mackenzie.waifuviewer.data.datasource.WaifusImLocalDataSource
import com.mackenzie.waifuviewer.data.datasource.WaifusImRemoteDataSource
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.WaifuImItem
import com.mackenzie.waifuviewer.ui.defaultFakeImWaifus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeLocalImDataSource : WaifusImLocalDataSource {

    val inMemoryWaifus = MutableStateFlow<List<WaifuImItem>>(emptyList())

    override val waifusIm = inMemoryWaifus

    private lateinit var findWaifuFlow: MutableStateFlow<WaifuImItem>

    override suspend fun isImEmpty() = waifusIm.value.isEmpty()

    override fun findImById(id: Int): Flow<WaifuImItem> {
        findWaifuFlow = MutableStateFlow(inMemoryWaifus.value.first { it.id == id })
        return findWaifuFlow
    }

    override suspend fun saveIm(waifus: List<WaifuImItem>): Error? {
        inMemoryWaifus.value = waifus

        if (::findWaifuFlow.isInitialized) {
            waifus.firstOrNull() { it.id == findWaifuFlow.value.id }
                ?.let { findWaifuFlow.value = it }
        }

        return null
    }

    override suspend fun saveOnlyIm(waifu: WaifuImItem): Error? {
        return null
    }

    override suspend fun deleteAll(): Error? {
        return null
    }
}

class FakeRemoteImDataSource : WaifusImRemoteDataSource {

    var waifus = defaultFakeImWaifus

    override suspend fun getRandomWaifusIm(
        isNsfw: Boolean, tag: String, isGif: Boolean, orientation: String) = waifus.right()

    override suspend fun getOnlyWaifuIm(): WaifuImItem? = waifus[0]
}