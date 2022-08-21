package com.mackenzie.waifuviewer.data

import com.mackenzie.waifuviewer.data.datasource.FavoriteLocalDataSource
import com.mackenzie.waifuviewer.domain.FavoriteItem
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
class FavoritesRepositoryTest {

    @Mock
    lateinit var favDataSource: FavoriteLocalDataSource

    lateinit var repo: FavoritesRepository

    private val localFavWaifu = flowOf(listOf(sampleFavWaifu.copy(id = 1)))

    @Before
    fun setUp() {
        whenever(favDataSource.favoriteWaifus).thenReturn(localFavWaifu)
        repo = FavoritesRepository(favDataSource)
    }

    @Test
    fun `waifus are taken from local datasource if available`(): Unit = runBlocking {
        val result = repo.savedFavorites
        assertEquals(localFavWaifu, result)
    }

    @Test
    fun `Switching favorite marks as favorite an unfavorite waifu`(): Unit = runBlocking {
        val waifu = sampleFavWaifu.copy(isFavorite = false)
        repo.switchFavorite(waifu)
        verify(favDataSource).save(argThat { this.isFavorite })
    }

    @Test
    fun `Switching favorite marks as unfavorite an favorite waifu`(): Unit = runBlocking {
        val waifu = sampleFavWaifu.copy(isFavorite = true)
        repo.switchFavorite(waifu)
        verify(favDataSource).save(argThat { !this.isFavorite })
    }

    @After
    fun tearDown() {
    }

    @Test
    fun getSavedFavorites() {
    }

    @Test
    fun findFavoriteById() {
    }

    @Test
    fun switchFavorite() {
    }

    @Test
    fun saveFavorite() {
    }

    @Test
    fun deleteFavorite() {
    }
}

private val sampleFavWaifu = FavoriteItem(
    id = 0,
    title = "Waifu Test",
    url = "https://cdn.waifu.im/5f7e656343cb7be1.jpg",
    isFavorite = true
)