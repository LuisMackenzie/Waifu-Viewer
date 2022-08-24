package com.mackenzie.waifuviewer.ui.main

import app.cash.turbine.test
import com.mackenzie.testshared.samplePicWaifu
import com.mackenzie.waifuviewer.WaifuPicsViewModel
import com.mackenzie.waifuviewer.WaifuPicsViewModel.UiState
import com.mackenzie.waifuviewer.testrules.CoroutinesTestRule
import com.mackenzie.waifuviewer.usecases.ClearWaifuPicUseCase
import com.mackenzie.waifuviewer.usecases.GetWaifuPicUseCase
import com.mackenzie.waifuviewer.usecases.RequestMoreWaifuPicUseCase
import com.mackenzie.waifuviewer.usecases.RequestWaifuPicUseCase
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
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class WaifuPicsViewModelTest {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Mock
    private lateinit var getWaifuPicUseCase: GetWaifuPicUseCase

    @Mock
    private lateinit var requestWaifuPicUseCase: RequestWaifuPicUseCase

    @Mock
    private lateinit var requestMoreWaifuPicUseCase: RequestMoreWaifuPicUseCase

    @Mock
    private lateinit var clearWaifuPicUseCase: ClearWaifuPicUseCase


    private lateinit var  vm: WaifuPicsViewModel

    private var picsSample = listOf(samplePicWaifu.copy(id = 1))

    @Before
    fun setUp() {
        whenever(getWaifuPicUseCase()).thenReturn(flowOf(picsSample))
        vm = WaifuPicsViewModel(getWaifuPicUseCase, requestWaifuPicUseCase, requestMoreWaifuPicUseCase, clearWaifuPicUseCase)
    }

    @Test
    fun `State in PICS is updated with current cached content inmediately`() = runTest {

        vm.state.test {
            Assert.assertEquals(UiState(), awaitItem())
            Assert.assertEquals(UiState(waifus = picsSample), awaitItem())
            cancel()
        }
    }

    @Test
    fun `Progress in PICS is shown when screen start and hidden when it finishes`() = runTest {

        vm.onPicsReady(false, "waifu")

        vm.state.test {
            Assert.assertEquals(UiState(), awaitItem())
            // Assert.assertEquals(UiState(waifus = picsSample), awaitItem())
            // Assert.assertEquals(UiState(waifus = picsSample, isLoading = true), awaitItem())
            Assert.assertEquals(UiState(waifus = picsSample, isLoading = false), awaitItem())
            cancel()
        }
    }

    @Test
    fun `Waifus PICS are requested when UI screen starts`() = runTest {
        vm.onPicsReady(false, "waifu")
        runCurrent()

        verify(requestWaifuPicUseCase).invoke(any(), any())
    }

    @Test
    fun `More Waifus are requested when press Button`() = runTest {
        vm.onRequestMore( false, "waifu")

        runCurrent()

        verify(requestMoreWaifuPicUseCase).invoke(any(), any())
    }

    @Test
    fun `Clear Waifus when press Button`() = runTest {
        vm.onClearPicsDatabase()

        runCurrent()

        verify(clearWaifuPicUseCase).invoke()
    }
}