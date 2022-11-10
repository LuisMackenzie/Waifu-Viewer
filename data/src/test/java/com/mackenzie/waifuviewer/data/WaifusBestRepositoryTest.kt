package com.mackenzie.waifuviewer.data

import arrow.core.right
import com.mackenzie.testshared.sampleBestWaifu
import com.mackenzie.waifuviewer.data.datasource.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
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
class WaifusBestRepositoryTest {

    @Mock
    private lateinit var localDataSource: WaifusBestLocalDataSource

    @Mock
    private lateinit var remoteDataSource: WaifusBestRemoteDataSource

    @Mock
    private lateinit var favDataSource: FavoriteLocalDataSource

    private lateinit var repo: WaifusBestRepository

    private val localBestWaifus = flowOf(listOf(sampleBestWaifu.copy(id = 1)))

    @Before
    fun setUp() {
        whenever(localDataSource.waifus).thenReturn(localBestWaifus)
        repo = WaifusBestRepository(localDataSource, favDataSource, remoteDataSource)
    }

    @Test
    fun `Waifus are taken from local datasource if available`() = runTest {
        val result = repo.savedWaifus
        Assert.assertEquals(localBestWaifus, result)
    }

    @Test
    fun `Finding a waifu by id is done in local data source`() = runTest {
        val waifu = flowOf(sampleBestWaifu.copy(id = 2))
        whenever(localDataSource.findById(2)).thenReturn(waifu)

        val result = repo.findById(2)

        Assert.assertEquals(waifu, result)
    }

    @Test
    fun `Waifus PNG are saved to local data source when it's empty`() = runTest {
        val remoteWaifu = listOf(sampleBestWaifu.copy(3))
        whenever(localDataSource.isEmpty()).thenReturn(true)
        // whenever(regionRepository.findLastRegion()).thenReturn(RegionRepository.DEFAULT_REGION)
        whenever(remoteDataSource.getRandomWaifusBestPng(any())).thenReturn(remoteWaifu.right())

        repo.requestWaifus("waifu")

        verify(localDataSource).save(remoteWaifu)
    }

    @Test
    fun `Waifus GIF are saved to local data source when it's empty`() = runTest {
        val remoteWaifu = listOf(sampleBestWaifu.copy(3))
        whenever(localDataSource.isEmpty()).thenReturn(true)
        // whenever(regionRepository.findLastRegion()).thenReturn(RegionRepository.DEFAULT_REGION)
        whenever(remoteDataSource.getRandomWaifusBestGif(any())).thenReturn(remoteWaifu.right())

        repo.requestWaifus("pat")

        verify(localDataSource).save(remoteWaifu)
    }

    @Test
    fun `More PNG waifus are saved to local data source`() = runTest {
        val remoteWaifu = listOf(sampleBestWaifu.copy(4))
        whenever(remoteDataSource.getRandomWaifusBestPng(any())).thenReturn(remoteWaifu.right())

        repo.requestNewWaifus("waifu")

        verify(localDataSource).save(remoteWaifu)
    }

    @Test
    fun `More GIF waifus are saved to local data source`() = runTest {
        val remoteWaifu = listOf(sampleBestWaifu.copy(4))
        whenever(remoteDataSource.getRandomWaifusBestGif(any())).thenReturn(remoteWaifu.right())

        repo.requestNewWaifus("pat")

        verify(localDataSource).save(remoteWaifu)
    }

    @Test
    fun `Waifus are delete from local data source`() = runTest {

        repo.requestClearWaifusBest()

        verify(localDataSource).deleteAll()
    }

    @Test
    fun `Only one PNG waifu are taken to server data source`() = runTest {
        val remoteWaifu = sampleBestWaifu.copy(6)
        whenever(remoteDataSource.getOnlyWaifuBestPng()).thenReturn(remoteWaifu)

        val result = repo.requestOnlyWaifu()

        Assert.assertEquals(remoteWaifu, result)
    }

    @Test
    fun `Switching favorite updates local data source`() = runTest {

        val waifu = sampleBestWaifu.copy(id = 7)

        repo.switchFavorite(waifu)

        verify(localDataSource).saveOnly(argThat { id == 7 })
    }


    @Test
    fun `Switching favorite marks as favorite an unfavorite waifu`() = runTest {
        val waifu = sampleBestWaifu.copy(isFavorite = false)

        repo.switchFavorite(waifu)

        verify(localDataSource).saveOnly(argThat { this.isFavorite })
    }

    @Test
    fun `Switching favorite marks as unfavorite an favorite waifu`() = runTest {
        val waifu = sampleBestWaifu.copy(isFavorite = true)

        repo.switchFavorite(waifu)

        verify(localDataSource).saveOnly(argThat { !this.isFavorite })
    }

}