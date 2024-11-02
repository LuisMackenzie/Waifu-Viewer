package com.mackenzie.waifuviewer.ui.detail

import app.cash.turbine.test
import com.mackenzie.testshared.sampleImWaifu
import com.mackenzie.waifuviewer.testrules.CoroutinesTestRule
import com.mackenzie.waifuviewer.ui.detail.DetailImViewModel.UiState
import com.mackenzie.waifuviewer.usecases.im.FindWaifuImUseCase
import com.mackenzie.waifuviewer.usecases.im.SwitchImFavoriteUseCase
import com.mackenzie.waifuviewer.usecases.moe.GetSearchMoeUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
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

    @Mock
    private lateinit var getSearchMoeUseCase: GetSearchMoeUseCase

    private lateinit var  vm: DetailImViewModel

    private var imSample = sampleImWaifu.copy(id = 7)

    @Before
    fun setUp() {
        whenever(findWaifuImUseCase(7)).thenReturn(flowOf(imSample))
        vm = DetailImViewModel(7, findWaifuImUseCase, switchImFavoriteUseCase, getSearchMoeUseCase)
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