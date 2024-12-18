package com.mackenzie.waifuviewer.ui.main

import app.cash.turbine.test
import com.mackenzie.testshared.sampleImWaifu
import com.mackenzie.testshared.samplePicWaifu
import com.mackenzie.waifuviewer.domain.ServerType
import com.mackenzie.waifuviewer.testrules.CoroutinesTestRule
import com.mackenzie.waifuviewer.ui.selector.SelectorViewModel
import com.mackenzie.waifuviewer.ui.selector.SelectorViewModel.UiState
import com.mackenzie.waifuviewer.usecases.best.RequestOnlyWaifuBestUseCase
import com.mackenzie.waifuviewer.usecases.im.GetWaifuImTagsUseCase
import com.mackenzie.waifuviewer.usecases.im.RequestOnlyWaifuImUseCase
import com.mackenzie.waifuviewer.usecases.im.RequestWaifuImTagsUseCase
import com.mackenzie.waifuviewer.usecases.pics.RequestOnlyWaifuPicUseCase
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
    private lateinit var getWaifuImTagsUseCase: GetWaifuImTagsUseCase

    @Mock
    private lateinit var requestOnlyWaifuImUseCase: RequestOnlyWaifuImUseCase

    @Mock
    private lateinit var requestOnlyWaifuPicUseCase: RequestOnlyWaifuPicUseCase

    @Mock
    private lateinit var requestOnlyWaifuBestUseCase: RequestOnlyWaifuBestUseCase

    @Mock
    private lateinit var requestWaifuImTagsUseCase: RequestWaifuImTagsUseCase

    private lateinit var  vm: SelectorViewModel

    private var imSample = sampleImWaifu.copy(id = 1)
    private var picsSample = samplePicWaifu.copy(id = 1)


    @Before
    fun setUp() {
        vm = SelectorViewModel(getWaifuImTagsUseCase, requestOnlyWaifuImUseCase,requestOnlyWaifuPicUseCase, requestOnlyWaifuBestUseCase, requestWaifuImTagsUseCase)
    }

    @Test
    fun `State IM is updated with current cached content inmediately`() = runTest {
        whenever(requestOnlyWaifuImUseCase(any())).thenReturn(imSample)

        vm.loadErrorOrWaifu(serverType = ServerType.NORMAL)

        vm.state.test {
            assertEquals(UiState(), awaitItem())
            assertEquals(UiState(waifuIm = imSample), awaitItem())
            cancel()
        }
    }

    @Test
    fun `Progress IM is shown when screen start and hidden when it finishes`() = runTest {

        whenever(requestOnlyWaifuImUseCase(any())).thenReturn(imSample)

        vm.loadErrorOrWaifu(serverType = ServerType.NORMAL)

        vm.state.test {
            assertEquals(UiState(), awaitItem())
            assertEquals(UiState(waifuIm = imSample), awaitItem())
            cancel()
        }
    }

    @Test
    fun `Waifu IM are requested when button Pressed`() = runTest {
        vm.loadErrorOrWaifu(serverType = ServerType.NORMAL)

        runCurrent()

        verify(requestOnlyWaifuImUseCase).invoke(any())
    }

    @Test
    fun `State PICS is updated with current cached content inmediately`() = runTest {
        whenever(requestOnlyWaifuPicUseCase()).thenReturn(picsSample)

        vm.loadErrorOrWaifu(serverType = ServerType.ENHANCED)

        vm.state.test {
            assertEquals(UiState(), awaitItem())
            assertEquals(UiState(waifuPic = picsSample), awaitItem())
            cancel()
        }
    }

    @Test
    fun `Progress PICS is shown when screen start and hidden when it finishes`() = runTest {

        whenever(requestOnlyWaifuPicUseCase()).thenReturn(picsSample)

        vm.loadErrorOrWaifu(serverType = ServerType.ENHANCED)

        vm.state.test {
            assertEquals(UiState(), awaitItem())
            assertEquals(UiState(waifuPic = picsSample), awaitItem())
            cancel()
        }
    }

    @Test
    fun `Waifu PICS are requested when button Pressed`() = runTest {
        vm.loadErrorOrWaifu(serverType = ServerType.ENHANCED)

        runCurrent()

        verify(requestOnlyWaifuPicUseCase).invoke()
    }

}