package com.mackenzie.waifuviewer.ui.fakes

import arrow.core.right
import com.mackenzie.waifuviewer.data.datasource.WaifusPicLocalDataSource
import com.mackenzie.waifuviewer.data.datasource.WaifusPicRemoteDataSource
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.WaifuPicItem
import com.mackenzie.waifuviewer.ui.defaultFakePicWaifus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeLocalPicDataSource : WaifusPicLocalDataSource {

    val inMemoryWaifus = MutableStateFlow<List<WaifuPicItem>>(emptyList())

    override val waifusPic = inMemoryWaifus

    private lateinit var findWaifuFlow: MutableStateFlow<WaifuPicItem>

    override suspend fun isPicsEmpty() = waifusPic.value.isEmpty()

    override fun findPicById(id: Int): Flow<WaifuPicItem> {
        findWaifuFlow = MutableStateFlow(inMemoryWaifus.value.first { it.id == id })
        return findWaifuFlow
    }

    override suspend fun savePics(waifus: List<WaifuPicItem>): Error? {
        inMemoryWaifus.value = waifus

        if (::findWaifuFlow.isInitialized) {
            waifus.firstOrNull() { it.id == findWaifuFlow.value.id }
                ?.let { findWaifuFlow.value = it }
        }

        return null
    }

    override suspend fun saveOnlyPics(waifu: WaifuPicItem): Error? {
        // inMemoryWaifus.value = listOf(waifu)
        return null
    }

    override suspend fun deleteAll(): Error? {
        return null
    }
}

class FakeRemotePicDataSource : WaifusPicRemoteDataSource {

    var waifus = defaultFakePicWaifus

    override suspend fun getRandomWaifusPics(isNsfw: String, tag: String) = waifus.right()

    override suspend fun getOnlyWaifuPics(): WaifuPicItem? = waifus[0]
}