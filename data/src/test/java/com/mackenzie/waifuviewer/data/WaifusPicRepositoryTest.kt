package com.mackenzie.waifuviewer.data

import arrow.core.right
import com.mackenzie.testshared.samplePicWaifu
import com.mackenzie.waifuviewer.data.datasource.FavoriteLocalDataSource
import com.mackenzie.waifuviewer.data.datasource.WaifusPicLocalDataSource
import com.mackenzie.waifuviewer.data.datasource.WaifusPicRemoteDataSource
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.argThat
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class WaifusPicRepositoryTest {

    @Mock
    lateinit var localDataSource: WaifusPicLocalDataSource

    @Mock
    lateinit var remoteDataSource: WaifusPicRemoteDataSource

    @Mock
    lateinit var favDataSource: FavoriteLocalDataSource

    lateinit var repo: WaifusPicRepository

    private val localPicWaifus = flowOf(listOf(samplePicWaifu.copy(id = 1)))

    @Before
    fun setUp() {
        whenever(localDataSource.waifusPic).thenReturn(localPicWaifus)
        repo = WaifusPicRepository(localDataSource, favDataSource, remoteDataSource)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `Waifus are taken from local datasource if available`(): Unit = runBlocking {
        val result = repo.savedWaifusPic
        assertEquals(localPicWaifus, result)
    }

    @Test
    fun `Finding a waifu by id is done in local data source`(): Unit = runBlocking {
        val waifu = flowOf(samplePicWaifu.copy(id = 2))
        whenever(localDataSource.findPicById(2)).thenReturn(waifu)

        val result = repo.findPicsById(2)

        assertEquals(waifu, result)
    }

    @Test
    fun `Waifus are saved to local data source when it's empty`(): Unit = runBlocking {
        val remoteWaifu = listOf(samplePicWaifu.copy(3))
        whenever(localDataSource.isPicsEmpty()).thenReturn(true)
        whenever(remoteDataSource.getRandomWaifusPics(any(), any())).thenReturn(remoteWaifu.right())

        repo.requestWaifusPics("sfw", "waifu")

        verify(localDataSource).savePics(remoteWaifu)
    }

    @Test
    fun `More waifus are saved to local data source`(): Unit = runBlocking {
        val remoteWaifu = listOf(samplePicWaifu.copy(4))
        whenever(remoteDataSource.getRandomWaifusPics(any(), any())).thenReturn(remoteWaifu.right())

        repo.requestNewWaifusPics("sfw", "waifu")

        verify(localDataSource).savePics(remoteWaifu)
    }

    @Test
    fun `Waifus are delete from local data source`(): Unit = runBlocking {

        repo.requestClearWaifusPic()

        verify(localDataSource).deleteAll()
    }

    @Test
    fun `Only waifu are taken to server data source`(): Unit = runBlocking {
        val remoteWaifu = samplePicWaifu.copy(5)
        whenever(remoteDataSource.getOnlyWaifuPics()).thenReturn(remoteWaifu)

        val result = repo.requestOnlyWaifuPic()

        assertEquals(remoteWaifu, result)
    }

    @Test
    fun `Switching favorite updates local data source`(): Unit = runBlocking {

        val movie = samplePicWaifu.copy(id = 6)

        repo.switchPicsFavorite(movie)

        verify(localDataSource).saveOnlyPics(argThat { id == 6 })
    }

    @Test
    fun `Switching favorite marks as favorite an unfavorite waifu`(): Unit = runBlocking {
        val waifu = samplePicWaifu.copy(isFavorite = false)
        repo.switchPicsFavorite(waifu)
        verify(localDataSource).saveOnlyPics(argThat { this.isFavorite })
    }

    @Test
    fun `Switching favorite marks as unfavorite an favorite waifu`(): Unit = runBlocking {
        val waifu = samplePicWaifu.copy(isFavorite = true)
        repo.switchPicsFavorite(waifu)
        verify(localDataSource).saveOnlyPics(argThat { !this.isFavorite })
    }

}