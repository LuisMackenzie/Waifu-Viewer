package com.mackenzie.waifuviewer.data

import arrow.core.right
import com.mackenzie.testshared.sampleImWaifu
import com.mackenzie.waifuviewer.data.datasource.FavoriteLocalDataSource
import com.mackenzie.waifuviewer.data.datasource.WaifusImLocalDataSource
import com.mackenzie.waifuviewer.data.datasource.WaifusImRemoteDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.argThat
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class WaifusImRepositoryTest {

    @Mock
    private lateinit var localDataSource: WaifusImLocalDataSource

    @Mock
    private lateinit var remoteDataSource: WaifusImRemoteDataSource

    @Mock
    private lateinit var favDataSource: FavoriteLocalDataSource

    private lateinit var repo: WaifusImRepository

    private val localImWaifus = flowOf(listOf(sampleImWaifu.copy(id = 1)))

    @Before
    fun setUp() {
        whenever(localDataSource.waifusIm).thenReturn(localImWaifus)
        repo = WaifusImRepository(localDataSource, favDataSource, remoteDataSource)
    }

    @Test
    fun `Waifus are taken from local datasource if available`() = runTest {
        val result = repo.savedWaifusIm
        assertEquals(localImWaifus, result)
    }

    @Test
    fun `Finding a waifu by id is done in local data source`() = runTest {
        val waifu = flowOf(sampleImWaifu.copy(id = 2))
        whenever(localDataSource.findImById(2)).thenReturn(waifu)

        val result = repo.findImById(2)

        assertEquals(waifu, result)
    }

    @Test
    fun `Waifus are saved to local data source when it's empty`() = runTest {
        val remoteWaifu = listOf(sampleImWaifu.copy(3))
        whenever(localDataSource.isImEmpty()).thenReturn(true)
        // whenever(regionRepository.findLastRegion()).thenReturn(RegionRepository.DEFAULT_REGION)
        whenever(remoteDataSource.getRandomWaifusIm(any(), any(), any(), any())).thenReturn(remoteWaifu.right())

        repo.requestWaifusIm(false, "waifu", false, false)

        verify(localDataSource).saveIm(remoteWaifu)
    }

    @Test
    fun `More waifus are saved to local data source`() = runTest {
        val remoteWaifu = listOf(sampleImWaifu.copy(4))
        whenever(remoteDataSource.getRandomWaifusIm(any(), any(), any(), any())).thenReturn(remoteWaifu.right())

        repo.requestNewWaifusIm(true, "waifu", false, false)

        verify(localDataSource).saveIm(remoteWaifu)
    }

    @Test
    fun `Waifus are delete from local data source`() = runTest {

        repo.requestClearWaifusIm()

        verify(localDataSource).deleteAll()
    }

    @Test
    fun `Only waifu are taken to server data source`() = runTest {
        val remoteWaifu = sampleImWaifu.copy(6)
        whenever(remoteDataSource.getOnlyWaifuIm(any())).thenReturn(remoteWaifu)

        val result = repo.requestOnlyWaifuIm(false)

        assertEquals(remoteWaifu, result)
    }

    @Test
    fun `Switching favorite updates local data source`() = runTest {

        val movie = sampleImWaifu.copy(id = 7)

        repo.switchImFavorite(movie)

        verify(localDataSource).saveOnlyIm(argThat { id == 7 })
    }


    @Test
    fun `Switching favorite marks as favorite an unfavorite waifu`() = runTest {
        val waifu = sampleImWaifu.copy(isFavorite = false)

        repo.switchImFavorite(waifu)

        verify(localDataSource).saveOnlyIm(argThat { this.isFavorite })
    }

    @Test
    fun `Switching favorite marks as unfavorite an favorite waifu`() = runTest {
        val waifu = sampleImWaifu.copy(isFavorite = true)

        repo.switchImFavorite(waifu)

        verify(localDataSource).saveOnlyIm(argThat { !this.isFavorite })
    }

}
