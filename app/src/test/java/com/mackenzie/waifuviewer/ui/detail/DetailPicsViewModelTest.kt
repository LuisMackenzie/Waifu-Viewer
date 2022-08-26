package com.mackenzie.waifuviewer.ui.detail

import app.cash.turbine.test
import com.mackenzie.testshared.samplePicWaifu
import com.mackenzie.waifuviewer.testrules.CoroutinesTestRule
import com.mackenzie.waifuviewer.ui.detail.DetailPicsViewModel.UiState
import com.mackenzie.waifuviewer.usecases.FindWaifuPicUseCase
import com.mackenzie.waifuviewer.usecases.SwitchPicFavoriteUseCase
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
class DetailPicsViewModelTest {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Mock
    private lateinit var findWaifuPicUseCase: FindWaifuPicUseCase

    @Mock
    private lateinit var switchPicFavoriteUseCase: SwitchPicFavoriteUseCase

    private lateinit var  vm: DetailPicsViewModel

    private var picSample = samplePicWaifu.copy(id = 9)

    @Before
    fun setUp() {
        whenever(findWaifuPicUseCase(9)).thenReturn(flowOf(picSample))
        vm = DetailPicsViewModel(9, findWaifuPicUseCase, switchPicFavoriteUseCase)
    }

    @Test
    fun `UI is updated with the movie on start`() = runTest {
        vm.state.test {
            assertEquals(UiState(), awaitItem())
            assertEquals(UiState(waifuPic = picSample), awaitItem())
            cancel()
        }
    }

    @Test
    fun `Favorite action calls the corresponding use case`() = runTest {

        vm.onFavoriteClicked()
        runCurrent()

        verify(switchPicFavoriteUseCase).invoke(picSample)
    }
}