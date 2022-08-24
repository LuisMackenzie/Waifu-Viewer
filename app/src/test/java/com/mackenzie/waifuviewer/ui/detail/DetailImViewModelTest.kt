package com.mackenzie.waifuviewer.ui.detail

import app.cash.turbine.test
import com.mackenzie.testshared.sampleImWaifu
import com.mackenzie.waifuviewer.testrules.CoroutinesTestRule
import com.mackenzie.waifuviewer.ui.detail.DetailImViewModel.UiState
import com.mackenzie.waifuviewer.ui.main.WaifuImViewModel
import com.mackenzie.waifuviewer.usecases.FindWaifuImUseCase
import com.mackenzie.waifuviewer.usecases.GetWaifuImUseCase
import com.mackenzie.waifuviewer.usecases.RequestWaifuImUseCase
import com.mackenzie.waifuviewer.usecases.SwitchImFavoriteUseCase
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
class DetailImViewModelTest {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Mock
    private lateinit var findWaifuImUseCase: FindWaifuImUseCase

    @Mock
    private lateinit var switchImFavoriteUseCase: SwitchImFavoriteUseCase

    private lateinit var  vm: DetailImViewModel

    private var imSample = sampleImWaifu.copy(id = 7)

    @Before
    fun setUp() {
        whenever(findWaifuImUseCase(7)).thenReturn(flowOf(imSample))
        vm = DetailImViewModel(7, findWaifuImUseCase, switchImFavoriteUseCase)
    }

    @Test
    fun `UI is updated with the movie on start`() = runTest {
        vm.state.test {
            assertEquals(UiState(), awaitItem())
            assertEquals(UiState(waifuIm = imSample), awaitItem())
            cancel()
        }
    }

    @Test
    fun `Favorite action calls the corresponding use case`() = runTest {

        vm.onFavoriteClicked()
        runCurrent()

        verify(switchImFavoriteUseCase).invoke(imSample)
    }

}