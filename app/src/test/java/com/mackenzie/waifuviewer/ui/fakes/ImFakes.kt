package com.mackenzie.waifuviewer.ui.fakes

import com.mackenzie.waifuviewer.data.db.WaifuImDao
import com.mackenzie.waifuviewer.data.db.WaifuImDbItem
import com.mackenzie.waifuviewer.data.server.Waifu
import com.mackenzie.waifuviewer.data.server.WaifuImService
import com.mackenzie.waifuviewer.data.server.WaifuResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeWaifuImDao(waifusIm: List<WaifuImDbItem> = emptyList()) : WaifuImDao {

    private val inMemoryMovies = MutableStateFlow(waifusIm)

    private lateinit var findWaifuFlow: MutableStateFlow<WaifuImDbItem>

    override fun getAllIm(): Flow<List<WaifuImDbItem>> = inMemoryMovies

    override fun findImById(id: Int): Flow<WaifuImDbItem> {
        findWaifuFlow = MutableStateFlow(inMemoryMovies.value.first { it.id == id })
        return findWaifuFlow
    }

    override suspend fun waifuImCount(): Int = inMemoryMovies.value.size

    override suspend fun insertWaifuIm(waifu: WaifuImDbItem) {
        TODO("Not yet implemented")
    }

    override suspend fun insertAllWaifuIm(waifus: List<WaifuImDbItem>) {
        inMemoryMovies.value = waifus

        if (::findWaifuFlow.isInitialized) {
            waifus.firstOrNull() { it.id == findWaifuFlow.value.id }
                ?.let { findWaifuFlow.value = it }
        }

    }

    override suspend fun updateWaifuIm(waifu: WaifuImDbItem) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteIm(waifu: WaifuImDbItem) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAll() {
        TODO("Not yet implemented")
    }

}

class FakeRemoteImService(private val waifus: List<Waifu> = emptyList()) : WaifuImService {

    override suspend fun getRandomWaifuIm(
        isNsfw: Boolean,
        tags: String,
        isGif: Boolean,
        orientation: String,
        manyWaifus: Boolean
    ) = WaifuResult(waifus)

    override suspend fun getOnlyRandomWaifuIm(
        isNsfw: Boolean,
        tags: String,
        isGif: Boolean,
        orientation: String,
        manyWaifus: Boolean
    ) = WaifuResult(waifus)

}

/*class FakeLocalImDataSource : WaifusImLocalDataSource {

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
}*/

/*class FakeRemoteImDataSource : WaifusImRemoteDataSource {

    var waifus = defaultFakeImWaifus

    override suspend fun getRandomWaifusIm(
        isNsfw: Boolean, tag: String, isGif: Boolean, orientation: String) = waifus.right()

    override suspend fun getOnlyWaifuIm(): WaifuImItem? = waifus[0]
}*/

