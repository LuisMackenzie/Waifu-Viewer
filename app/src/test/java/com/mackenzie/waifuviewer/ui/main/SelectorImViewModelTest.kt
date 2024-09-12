package com.mackenzie.waifuviewer.ui.main

import app.cash.turbine.test
import com.mackenzie.testshared.sampleImWaifu
import com.mackenzie.waifuviewer.testrules.CoroutinesTestRule
import com.mackenzie.waifuviewer.ui.main.SelectorImViewModel.UiState
import com.mackenzie.waifuviewer.usecases.im.RequestOnlyWaifuImUseCase
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
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SelectorImViewModelTest {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Mock
    private lateinit var requestOnlyWaifuImUseCase: RequestOnlyWaifuImUseCase

    private lateinit var  vm: SelectorImViewModel

    private var imSample = sampleImWaifu.copy(id = 1)


    @Before
    fun setUp() {
        vm = SelectorImViewModel(requestOnlyWaifuImUseCase)
    }

    @Test
    fun `State is updated with current cached content inmediately`() = runTest {
        whenever(requestOnlyWaifuImUseCase(any())).thenReturn(imSample)

        vm.loadErrorOrWaifu()

        vm.state.test {
            assertEquals(UiState(), awaitItem())
            assertEquals(UiState(waifu = imSample), awaitItem())
            cancel()
        }
    }

    @Test
    fun `Progress is shown when screen start and hidden when it finishes`() = runTest {

        whenever(requestOnlyWaifuImUseCase(any())).thenReturn(imSample)

        vm.loadErrorOrWaifu()

        vm.state.test {
            assertEquals(UiState(), awaitItem())
            assertEquals(UiState(waifu = imSample), awaitItem())
            cancel()
        }
    }

    @Test
    fun `Waifu are requested when button Pressed`() = runTest {
        vm.loadErrorOrWaifu()

        runCurrent()

        verify(requestOnlyWaifuImUseCase).invoke(any())
    }

    @Test
    fun onChangeType() {
    }

}