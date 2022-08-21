package com.mackenzie.waifuviewer.data

import com.mackenzie.waifuviewer.data.datasource.FavoriteLocalDataSource
import com.mackenzie.waifuviewer.data.datasource.WaifusPicLocalDataSource
import com.mackenzie.waifuviewer.data.datasource.WaifusPicRemoteDataSource
import com.mackenzie.waifuviewer.domain.WaifuPicItem
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
    fun `waifus are taken from local datasource if available`(): Unit = runBlocking {
        val result = repo.savedWaifusPic
        assertEquals(localPicWaifus, result)
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

    @Test
    fun getSavedWaifusPic() {
    }

    @Test
    fun findPicsById() {
    }

    @Test
    fun requestWaifusPics() {
    }

    @Test
    fun requestNewWaifusPics() {
    }

    @Test
    fun requestOnlyWaifuPic() {
    }

    @Test
    fun requestClearWaifusPic() {
    }

    @Test
    fun switchPicsFavorite() {
    }
}

private val samplePicWaifu = WaifuPicItem(
    id = 0,
    url = "https://cdn.waifu.im/5f7e656343cb7be1.jpg",
    isFavorite = true
)