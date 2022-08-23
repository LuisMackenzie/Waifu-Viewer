package com.mackenzie.waifuviewer.ui.favs

import com.mackenzie.testshared.sampleFavWaifu
import com.mackenzie.waifuviewer.CoroutinesTestRule
import com.mackenzie.waifuviewer.usecases.DeleteFavoriteUseCase
import com.mackenzie.waifuviewer.usecases.GetFavoritesUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.Assert.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class FavoriteViewModelTest {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Mock
    private lateinit var getFavoritesUseCase: GetFavoritesUseCase

    @Mock
    private lateinit var deleteFavoriteUseCase: DeleteFavoriteUseCase

    private lateinit var  vm: FavoriteViewModel

    private var favoriteSample = listOf(sampleFavWaifu.copy(id = 1))

    @Before
    fun setUp() {
        whenever(getFavoritesUseCase()).thenReturn(flowOf(favoriteSample))
        vm = FavoriteViewModel(getFavoritesUseCase, deleteFavoriteUseCase)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `State is updated with current cached content inmediately`() = runTest {
        /*viewModel.state.collect {
            assertEquals(FavoriteViewModel.UiState(waifus = favoriteSample), it)
        }*/
        val results = mutableListOf<FavoriteViewModel.UiState>()
        val job = launch { vm.state.toList(results) }
        runCurrent()
        job.cancel()
        assertEquals(FavoriteViewModel.UiState(waifus = favoriteSample), results[0])
    }

    @Test
    fun `Progress is shown when screen start and hidden when it finishes`() = runTest {
        val results = mutableListOf<FavoriteViewModel.UiState>()
        val job = launch { vm.state.toList(results) }
        runCurrent()
        job.cancel()
        assertEquals(FavoriteViewModel.UiState(waifus = favoriteSample), results[0])
    }

    @Test
    fun getState() {
    }

    @Test
    fun onDeleteFavorite() {
    }

    @Test
    fun getCoroutineContext() {
    }

    @Test
    fun getJob() {
    }

    @Test
    fun setJob() {
    }

    @Test
    fun destroyScope() {
    }

    @Test
    fun initScope() {
    }
}