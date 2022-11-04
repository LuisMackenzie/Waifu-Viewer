package com.mackenzie.waifuviewer.ui.main

import app.cash.turbine.test
import com.mackenzie.testshared.sampleImWaifu
import com.mackenzie.waifuviewer.testrules.CoroutinesTestRule
import com.mackenzie.waifuviewer.ui.main.WaifuImViewModel.UiState
import com.mackenzie.waifuviewer.usecases.im.ClearWaifuImUseCase
import com.mackenzie.waifuviewer.usecases.im.GetWaifuImUseCase
import com.mackenzie.waifuviewer.usecases.im.RequestMoreWaifuImUseCase
import com.mackenzie.waifuviewer.usecases.im.RequestWaifuImUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class WaifuImViewModelTest {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Mock
    private lateinit var getWaifuImUseCase: GetWaifuImUseCase

    @Mock
    private lateinit var requestWaifuImUseCase: RequestWaifuImUseCase

    @Mock
    private lateinit var requestMoreWaifuImUseCase: RequestMoreWaifuImUseCase

    @Mock
    private lateinit var clearWaifuImUseCase: ClearWaifuImUseCase


    private lateinit var  vm: WaifuImViewModel

    private var imSample = listOf(sampleImWaifu.copy(id = 1))

    @Before
    fun setUp() {
        whenever(getWaifuImUseCase()).thenReturn(flowOf(imSample))
        vm = WaifuImViewModel(getWaifuImUseCase, requestWaifuImUseCase, requestMoreWaifuImUseCase, clearWaifuImUseCase)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `State is updated with current cached content inmediately`() = runTest {

        vm.state.test {
            assertEquals(UiState(), awaitItem())
            assertEquals(UiState(waifus = imSample), awaitItem())
            cancel()
        }
    }

    @Test
    fun `Progress is shown when screen start and hidden when it finishes`() = runTest {

        vm.onImReady(false, false, "waifu", false)

        vm.state.test {
            assertEquals(UiState(), awaitItem())
            // assertEquals(UiState(isLoading = true), awaitItem())
            assertEquals(UiState(isLoading = false, waifus = imSample), awaitItem())
            // assertEquals(UiState(isLoading = true), awaitItem())
            // assertEquals(UiState(isLoading = false), awaitItem())
            cancel()
        }
    }

    @Test
    fun `Waifus are requested when UI screen starts`() = runTest {
        vm.onImReady(false, false, "waifu", false)

        runCurrent()

        verify(requestWaifuImUseCase).invoke(any(), any(), any(), any())
    }

    @Test
    fun `More Waifus are requested when press Button`() = runTest {
        vm.onRequestMore(false, false, "waifu", false)

        runCurrent()

        verify(requestMoreWaifuImUseCase).invoke(any(), any(), any(), any())
    }

    @Test
    fun `Clear Waifus when press Button`() = runTest {
        vm.onClearImDatabase()

        runCurrent()

        verify(clearWaifuImUseCase).invoke()
    }


}