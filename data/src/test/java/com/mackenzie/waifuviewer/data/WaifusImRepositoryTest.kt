package com.mackenzie.waifuviewer.data

import com.mackenzie.waifuviewer.data.datasource.FavoriteLocalDataSource
import com.mackenzie.waifuviewer.data.datasource.WaifusImLocalDataSource
import com.mackenzie.waifuviewer.data.datasource.WaifusImRemoteDataSource
import com.mackenzie.waifuviewer.domain.WaifuImItem
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.argThat
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class WaifusImRepositoryTest {

    @Mock
    private lateinit var localDataSource: WaifusImLocalDataSource

    @Mock
    private lateinit var favDataSource: FavoriteLocalDataSource

    @Mock
    private lateinit var remoteDataSource: WaifusImRemoteDataSource

    private lateinit var repo: WaifusImRepository

    private val localImWaifus = flowOf(listOf(sampleImWaifu.copy(id = 1)))

    @Before
    fun setUp() {
        whenever(localDataSource.waifusIm).thenReturn(localImWaifus)
        repo = WaifusImRepository(localDataSource, favDataSource, remoteDataSource)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `waifus are taken from local datasource if available`(): Unit = runBlocking {
        val result = repo.savedWaifusIm
        assertEquals(localImWaifus, result)
    }

    @Test
    fun `Switching favorite marks as favorite an unfavorite waifu`(): Unit = runBlocking {
        val waifu = sampleImWaifu.copy(isFavorite = false)
        repo.switchImFavorite(waifu)
        verify(localDataSource).saveOnlyIm(argThat { this.isFavorite })
    }

    @Test
    fun getSavedWaifusIm() {
    }

    @Test
    fun findImById() {
    }

    @Test
    fun requestWaifusIm() {
    }

    @Test
    fun requestNewWaifusIm() {
    }

    @Test
    fun requestClearWaifusIm() {
    }

    @Test
    fun requestOnlyWaifuIm() {
    }

    @Test
    fun switchImFavorite() {
    }

}

private val sampleImWaifu = WaifuImItem(
    id = 0,
    dominantColor = "",
    file = "",
    height = "",
    width = "",
    imageId = 6969,
    isNsfw = false,
    url = "https://cdn.waifu.im/5f7e656343cb7be1.jpg",
    isFavorite = true
)