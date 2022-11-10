package com.mackenzie.waifuviewer.ui.detail

import app.cash.turbine.test
import com.mackenzie.testshared.sampleBestWaifu
import com.mackenzie.waifuviewer.testrules.CoroutinesTestRule
import com.mackenzie.waifuviewer.usecases.best.FindWaifuBestUseCase
import com.mackenzie.waifuviewer.usecases.best.SwitchBestFavoriteUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert
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
class DetailBestViewModelTest {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Mock
    private lateinit var findWaifuUseCase: FindWaifuBestUseCase

    @Mock
    private lateinit var switchFavoriteUseCase: SwitchBestFavoriteUseCase

    private lateinit var  vm: DetailBestViewModel

    private var bestSample = sampleBestWaifu.copy(id = 9)

    @Before
    fun setUp() {
        whenever(findWaifuUseCase(9)).thenReturn(flowOf(bestSample))
        vm = DetailBestViewModel(9, findWaifuUseCase, switchFavoriteUseCase)
    }

    @Test
    fun `UI is updated with the movie on start`() = runTest {
        vm.state.test {
            Assert.assertEquals(DetailBestViewModel.UiState(), awaitItem())
            Assert.assertEquals(DetailBestViewModel.UiState(waifu = bestSample), awaitItem())
            cancel()
        }
    }

    @Test
    fun `Favorite action calls the corresponding use case`() = runTest {

        vm.onFavoriteClicked()

        runCurrent()

        verify(switchFavoriteUseCase).invoke(bestSample)
    }

}