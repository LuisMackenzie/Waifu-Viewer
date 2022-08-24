package com.mackenzie.waifuviewer.ui.main

import app.cash.turbine.test
import com.mackenzie.testshared.samplePicWaifu
import com.mackenzie.waifuviewer.testrules.CoroutinesTestRule
import com.mackenzie.waifuviewer.ui.SelectorPicViewModel
import com.mackenzie.waifuviewer.ui.SelectorPicViewModel.UiState
import com.mackenzie.waifuviewer.usecases.RequestOnlyWaifuPicUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class SelectorPicViewModelTest {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Mock
    private lateinit var requestOnlyWaifuPicUseCase: RequestOnlyWaifuPicUseCase

    private lateinit var  vm: SelectorPicViewModel

    private var picsSample = samplePicWaifu.copy(id = 1)

    @Before
    fun setUp() {
        vm = SelectorPicViewModel(requestOnlyWaifuPicUseCase)
    }

    @Test
    fun `State is updated with current cached content inmediately`() = runTest {
        whenever(requestOnlyWaifuPicUseCase()).thenReturn(picsSample)

        vm.loadErrorOrWaifu()

        vm.state.test {
            assertEquals(UiState(), awaitItem())
            assertEquals(UiState(waifu = picsSample), awaitItem())
            cancel()
        }
    }

    @Test
    fun `Progress is shown when screen start and hidden when it finishes`() = runTest {

        whenever(requestOnlyWaifuPicUseCase()).thenReturn(picsSample)

        vm.loadErrorOrWaifu()

        vm.state.test {
            assertEquals(UiState(), awaitItem())
            assertEquals(UiState(waifu = picsSample), awaitItem())
            cancel()
        }
    }

    @Test
    fun `Waifu are requested when button Pressed`() = runTest {
        vm.loadErrorOrWaifu()

        runCurrent()

        verify(requestOnlyWaifuPicUseCase).invoke()
    }

    @Test
    fun onChangeType() {
    }

}