package com.mackenzie.waifuviewer.data

import com.mackenzie.testshared.sampleFavWaifu
import com.mackenzie.waifuviewer.data.datasource.FavoriteLocalDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.argThat
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
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
    fun `waifus are taken from local datasource if available`() = runTest {
        val result = repo.savedFavorites
        assertEquals(localFavWaifu, result)
    }

    @Test
    fun `Finding a waifu by id is done in local data source`() = runTest {
        val waifu = flowOf(sampleFavWaifu.copy(id = 2))
        whenever(favDataSource.findFavById(2)).thenReturn(waifu)

        val result = repo.findFavoriteById(2)

        assertEquals(waifu, result)
    }

    @Test
    fun `Waifus are saved in local data source`() = runTest {
        val waifu = sampleFavWaifu.copy(id = 3)
        // whenever(favDataSource.delete(any())).thenReturn(waifu)

        repo.saveFavorite(waifu)

        verify(favDataSource).save(argThat { id == 3 })
    }

    @Test
    fun `Waifus are delete from local data source`() = runTest {
        val waifu = sampleFavWaifu.copy(id = 4)
        // whenever(favDataSource.delete(any())).thenReturn(waifu)

        repo.deleteFavorite(waifu)

        verify(favDataSource).delete(argThat { id == 4 })
    }

    @Test
    fun `Switching favorite updates local data source`() = runTest {

        val movie = sampleFavWaifu.copy(id = 5)

        repo.switchFavorite(movie)

        verify(favDataSource).save(argThat { id == 5 })
    }

    @Test
    fun `Switching favorite marks as favorite an unfavorite waifu`() = runTest {
        val waifu = sampleFavWaifu.copy(isFavorite = false)
        repo.switchFavorite(waifu)
        verify(favDataSource).save(argThat { this.isFavorite })
    }

    @Test
    fun `Switching favorite marks as unfavorite an favorite waifu`() = runTest {
        val waifu = sampleFavWaifu.copy(isFavorite = true)
        repo.switchFavorite(waifu)
        verify(favDataSource).save(argThat { !this.isFavorite })
    }

}