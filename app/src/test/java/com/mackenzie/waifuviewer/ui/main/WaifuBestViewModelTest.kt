package com.mackenzie.waifuviewer.ui.main

import app.cash.turbine.test
import com.mackenzie.testshared.sampleBestWaifu
import com.mackenzie.waifuviewer.testrules.CoroutinesTestRule
import com.mackenzie.waifuviewer.usecases.best.ClearWaifuBestUseCase
import com.mackenzie.waifuviewer.usecases.best.GetWaifuBestUseCase
import com.mackenzie.waifuviewer.usecases.best.RequestMoreWaifuBestUseCase
import com.mackenzie.waifuviewer.usecases.best.RequestWaifuBestUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class WaifuBestViewModelTest {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Mock
    private lateinit var getWaifuUseCase: GetWaifuBestUseCase

    @Mock
    private lateinit var requestWaifuUseCase: RequestWaifuBestUseCase

    @Mock
    private lateinit var requestMoreWaifuUseCase: RequestMoreWaifuBestUseCase

    @Mock
    private lateinit var clearWaifuUseCase: ClearWaifuBestUseCase


    private lateinit var  vm: WaifuBestViewModel

    private var bestSample = listOf(sampleBestWaifu.copy(id = 1))

    @Before
    fun setUp() {
        whenever(getWaifuUseCase()).thenReturn(flowOf(bestSample))
        vm = WaifuBestViewModel(getWaifuUseCase, requestWaifuUseCase, requestMoreWaifuUseCase, clearWaifuUseCase)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `State is updated with current cached content inmediately`() = runTest {

        vm.state.test {
            Assert.assertEquals(WaifuBestViewModel.UiState(), awaitItem())
            Assert.assertEquals(WaifuBestViewModel.UiState(waifus = bestSample), awaitItem())
            cancel()
        }
    }

    @Test
    fun `Progress is shown when screen start and hidden when it finishes`() = runTest {

        vm.onBestReady("waifu")

        vm.state.test {
            Assert.assertEquals(WaifuBestViewModel.UiState(), awaitItem())
            // assertEquals(UiState(isLoading = true), awaitItem())
            Assert.assertEquals(WaifuBestViewModel.UiState(isLoading = false, waifus = bestSample), awaitItem())
            // assertEquals(UiState(isLoading = true), awaitItem())
            // assertEquals(UiState(isLoading = false), awaitItem())
            cancel()
        }
    }

    @Test
    fun `Waifus are requested when UI screen starts`() = runTest {
        vm.onBestReady("waifu")

        runCurrent()

        verify(requestWaifuUseCase).invoke(any())
    }

    @Test
    fun `More Waifus are requested when press Button`() = runTest {
        vm.onRequestMore("waifu")

        runCurrent()

        verify(requestMoreWaifuUseCase).invoke(any())
    }

    @Test
    fun `Clear Waifus when press Button`() = runTest {
        vm.onClearDatabase()

        runCurrent()

        verify(clearWaifuUseCase).invoke()
    }

}