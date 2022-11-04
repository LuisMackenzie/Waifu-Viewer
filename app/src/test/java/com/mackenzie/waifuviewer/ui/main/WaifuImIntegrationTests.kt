package com.mackenzie.waifuviewer.ui.main

import app.cash.turbine.test
import com.mackenzie.waifuviewer.data.db.WaifuImDbItem
import com.mackenzie.waifuviewer.data.server.WaifuIm
import com.mackenzie.waifuviewer.testrules.CoroutinesTestRule
import com.mackenzie.waifuviewer.ui.buildImDatabaseWaifus
import com.mackenzie.waifuviewer.ui.buildImRepositoryWith
import com.mackenzie.waifuviewer.ui.buildImRemoteWaifus
import com.mackenzie.waifuviewer.ui.main.WaifuImViewModel.UiState
import com.mackenzie.waifuviewer.usecases.im.ClearWaifuImUseCase
import com.mackenzie.waifuviewer.usecases.im.GetWaifuImUseCase
import com.mackenzie.waifuviewer.usecases.im.RequestMoreWaifuImUseCase
import com.mackenzie.waifuviewer.usecases.im.RequestWaifuImUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class WaifuImIntegrationTests  {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Test
    fun `Data is loaded from IM server when local source is empty`() = runTest {
        val remoteData = buildImRemoteWaifus(4, 5, 6)

        val vm = buildModelWith(localData = emptyList(), remoteData = remoteData)

        vm.onImReady(false, false, "waifu", false)


        vm.state.test {
            assertEquals(UiState(), awaitItem())
            // Assert.assertEquals(UiState(waifus = emptyList()), awaitItem())
            // Assert.assertEquals(UiState(waifus = emptyList(), isLoading = true), awaitItem())
            assertEquals(UiState(waifus = emptyList(), isLoading = false), awaitItem())
            // assertEquals(UiState(waifus = remoteData), awaitItem())

            val waifus = awaitItem().waifus!!
            assertEquals("Overview 4", waifus[0].file)
            assertEquals("Overview 5", waifus[1].file)
            assertEquals("Overview 6", waifus[2].file)

            cancel()
        }

    }

    @Test
    fun `Data is loaded from local IM database when available`() = runTest {
        val localData = buildImDatabaseWaifus(1, 2, 3)
        val remoteData = buildImRemoteWaifus(4, 5, 6)

        val vm = buildModelWith(localData, remoteData)

        vm.onImReady(false, false, "waifu", false)


        vm.state.test {
            assertEquals(UiState(), awaitItem())
            val waifus = awaitItem().waifus!!
            assertEquals("Overview 1", waifus[0].file)
            assertEquals("Overview 2", waifus[1].file)
            assertEquals("Overview 3", waifus[2].file)
            cancel()
        }

    }



    private fun buildModelWith(localData:List<WaifuImDbItem> = emptyList(), remoteData: List<WaifuIm> = emptyList()): WaifuImViewModel {

        val repo = buildImRepositoryWith(localData, remoteData)

        val getWaifuImUseCase = GetWaifuImUseCase(repo)
        val requestWaifuImUseCase = RequestWaifuImUseCase(repo)
        val requestMoreWaifuImUseCase = RequestMoreWaifuImUseCase(repo)
        val clearWaifuImUseCase = ClearWaifuImUseCase(repo)
        val vm = WaifuImViewModel(getWaifuImUseCase, requestWaifuImUseCase, requestMoreWaifuImUseCase, clearWaifuImUseCase)
        return vm
    }

}