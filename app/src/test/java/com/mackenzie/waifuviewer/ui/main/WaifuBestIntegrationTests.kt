package com.mackenzie.waifuviewer.ui.main

import app.cash.turbine.test
import com.mackenzie.waifuviewer.data.db.WaifuBestDbItem
import com.mackenzie.waifuviewer.data.server.models.WaifuBestGif
import com.mackenzie.waifuviewer.data.server.models.WaifuBestPng
import com.mackenzie.waifuviewer.testrules.CoroutinesTestRule
import com.mackenzie.waifuviewer.ui.helpers.buildBestDatabaseWaifus
import com.mackenzie.waifuviewer.ui.helpers.buildBestRemoteWaifus
import com.mackenzie.waifuviewer.ui.helpers.buildBestRepositoryWith
import com.mackenzie.waifuviewer.ui.main.WaifuBestViewModel.UiState
import com.mackenzie.waifuviewer.usecases.best.ClearWaifuBestUseCase
import com.mackenzie.waifuviewer.usecases.best.GetWaifuBestUseCase
import com.mackenzie.waifuviewer.usecases.best.RequestMoreWaifuBestUseCase
import com.mackenzie.waifuviewer.usecases.best.RequestWaifuBestUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class WaifuBestIntegrationTests {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Test
    fun `Data is loaded from BEST server when local source is empty`() = runTest {
        val remoteData = buildBestRemoteWaifus(4, 5, 6)

        val vm = buildModelWith(localData = emptyList(), remoteData = remoteData)

        vm.onBestReady("waifu")


        vm.state.test {
            Assert.assertEquals(UiState(), awaitItem())
            // Assert.assertEquals(UiState(waifus = emptyList()), awaitItem())
            // Assert.assertEquals(UiState(waifus = emptyList(), isLoading = true), awaitItem())
            Assert.assertEquals(UiState(waifus = emptyList(), isLoading = false), awaitItem())
            // assertEquals(UiState(waifus = remoteData), awaitItem())

            val waifus = awaitItem().waifus!!
            Assert.assertEquals("Overview 4", waifus[0].artistName)
            Assert.assertEquals("Overview 5", waifus[1].artistName)
            Assert.assertEquals("Overview 6", waifus[2].artistName)

            cancel()
        }

    }

    @Test
    fun `Data is loaded from local BEST database when available`() = runTest {
        val localData = buildBestDatabaseWaifus(1, 2, 3)
        val remoteData = buildBestRemoteWaifus(4, 5, 6)

        val vm = buildModelWith(localData, remoteData)

        vm.onBestReady("waifu")


        vm.state.test {
            Assert.assertEquals(UiState(), awaitItem())
            val waifus = awaitItem().waifus!!
            Assert.assertEquals("Overview 1", waifus[0].artistName)
            Assert.assertEquals("Overview 2", waifus[1].artistName)
            Assert.assertEquals("Overview 3", waifus[2].artistName)
            cancel()
        }

    }



    private fun buildModelWith(
        localData:List<WaifuBestDbItem> = emptyList(),
        remoteData: List<WaifuBestPng> = emptyList(),
        remoteDataGif: List<WaifuBestGif> = emptyList()): WaifuBestViewModel {

        val repo = buildBestRepositoryWith(localData, remoteData, remoteDataGif)

        val getWaifuUseCase = GetWaifuBestUseCase(repo)
        val requestWaifuUseCase = RequestWaifuBestUseCase(repo)
        val requestMoreWaifuUseCase = RequestMoreWaifuBestUseCase(repo)

        val vm = WaifuBestViewModel(getWaifuUseCase, requestWaifuUseCase, requestMoreWaifuUseCase)
        return vm
    }

}