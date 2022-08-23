package com.mackenzie.waifuviewer.ui.main

import app.cash.turbine.test
import com.mackenzie.testshared.sampleImWaifu
import com.mackenzie.waifuviewer.CoroutinesTestRule
import com.mackenzie.waifuviewer.ui.main.WaifuImViewModel.UiState
import com.mackenzie.waifuviewer.usecases.ClearWaifuImUseCase
import com.mackenzie.waifuviewer.usecases.GetWaifuImUseCase
import com.mackenzie.waifuviewer.usecases.RequestMoreWaifuImUseCase
import com.mackenzie.waifuviewer.usecases.RequestWaifuImUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
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
            assertEquals(UiState(waifus = imSample), awaitItem())
            val job = launch { assertEquals(UiState(waifus = imSample, isLoading = true), awaitItem()) }
            val job2 = launch { assertEquals(UiState(waifus = imSample, isLoading = false), awaitItem()) }
            runCurrent()
            job.cancel()
            job2.cancel()
            cancel()
        }
    }

    @Test
    fun getState() {
    }

    @Test
    fun onImReady() {
    }

    @Test
    fun onRequestMore() {
    }

    @Test
    fun onClearImDatabase() {
    }

    @Test
    fun getCoroutineContext() {
    }

    @Test
    fun getJob() {
    }

    @Test
    fun setJob() {
    }

    @Test
    fun destroyScope() {
    }

    @Test
    fun initScope() {
    }
}