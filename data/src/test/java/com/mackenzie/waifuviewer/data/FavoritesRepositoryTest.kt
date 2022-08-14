package com.mackenzie.waifuviewer.data

import com.mackenzie.waifuviewer.data.datasource.FavoriteLocalDataSource
import com.mackenzie.waifuviewer.domain.FavoriteItem
import com.mackenzie.waifuviewer.domain.WaifuPicItem
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class FavoritesRepositoryTest {

    @Mock
    lateinit var favDataSource: FavoriteLocalDataSource

    lateinit var repository: FavoritesRepository

    @Before
    fun setUp() {
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