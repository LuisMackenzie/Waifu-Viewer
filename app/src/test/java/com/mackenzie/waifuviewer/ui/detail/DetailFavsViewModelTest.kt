package com.mackenzie.waifuviewer.ui.detail

import app.cash.turbine.test
import com.mackenzie.testshared.sampleFavWaifu
import com.mackenzie.testshared.sampleImWaifu
import com.mackenzie.waifuviewer.testrules.CoroutinesTestRule
import com.mackenzie.waifuviewer.ui.detail.DetailFavsViewModel.UiState
import com.mackenzie.waifuviewer.usecases.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class DetailFavsViewModelTest {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Mock
    private lateinit var findFavoriteUseCase: FindFavoriteUseCase

    @Mock
    private lateinit var switchFavoriteUseCase: SwitchFavoriteUseCase

    private lateinit var  vm: DetailFavsViewModel

    private var favSample = sampleFavWaifu.copy(id = 8)

    @Before
    fun setUp() {
        whenever(findFavoriteUseCase(8)).thenReturn(flowOf(favSample))
        vm = DetailFavsViewModel(8, findFavoriteUseCase, switchFavoriteUseCase)
    }

    @Test
    fun `UI is updated with the movie on start`() = runTest {
        vm.state.test {
            assertEquals(UiState(), awaitItem())
            assertEquals(UiState(waifu = favSample), awaitItem())
            cancel()
        }
    }

    @Test
    fun `Favorite action calls the corresponding use case`() = runTest {
        vm.onFavoriteClicked()

        runCurrent()

        verify(switchFavoriteUseCase).invoke(favSample)
    }

}