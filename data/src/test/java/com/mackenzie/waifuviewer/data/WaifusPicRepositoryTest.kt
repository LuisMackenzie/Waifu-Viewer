package com.mackenzie.waifuviewer.data

import com.mackenzie.waifuviewer.data.datasource.FavoriteLocalDataSource
import com.mackenzie.waifuviewer.data.datasource.WaifusPicLocalDataSource
import com.mackenzie.waifuviewer.data.datasource.WaifusPicRemoteDataSource
import com.mackenzie.waifuviewer.domain.WaifuPicItem
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class WaifusPicRepositoryTest {

    @Mock
    lateinit var localDataSource: WaifusPicLocalDataSource

    @Mock
    lateinit var remoteDataSource: WaifusPicRemoteDataSource

    @Mock
    lateinit var favDataSource: FavoriteLocalDataSource

    lateinit var repository: WaifusPicRepository

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
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