package com.mackenzie.waifuviewer.ui.main

import app.cash.turbine.test
import com.mackenzie.testshared.sampleImWaifu
import com.mackenzie.waifuviewer.data.RegionRepository
import com.mackenzie.waifuviewer.data.WaifusImRepository
import com.mackenzie.waifuviewer.domain.WaifuImItem
import com.mackenzie.waifuviewer.testrules.CoroutinesTestRule
import com.mackenzie.waifuviewer.ui.FakeLocationDataSource
import com.mackenzie.waifuviewer.ui.FakePermissionChecker
import com.mackenzie.waifuviewer.ui.fakes.FakeFavoriteDataSource
import com.mackenzie.waifuviewer.ui.fakes.FakeLocalImDataSource
import com.mackenzie.waifuviewer.ui.fakes.FakeRemoteImDataSource
import com.mackenzie.waifuviewer.ui.main.WaifuImViewModel.*
import com.mackenzie.waifuviewer.usecases.ClearWaifuImUseCase
import com.mackenzie.waifuviewer.usecases.GetWaifuImUseCase
import com.mackenzie.waifuviewer.usecases.RequestMoreWaifuImUseCase
import com.mackenzie.waifuviewer.usecases.RequestWaifuImUseCase
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
        val remoteData = listOf(sampleImWaifu.copy(1), sampleImWaifu.copy(2), sampleImWaifu.copy(3))

        val vm = buildModelWith(remoteData = remoteData)

        vm.onImReady(false, false, "waifu", false)


        vm.state.test {
            assertEquals(UiState(), awaitItem())
            // Assert.assertEquals(UiState(waifus = emptyList()), awaitItem())
            // Assert.assertEquals(UiState(waifus = emptyList(), isLoading = true), awaitItem())
            assertEquals(UiState(waifus = emptyList(), isLoading = false), awaitItem())
            assertEquals(UiState(waifus = remoteData), awaitItem())
        }

    }

    @Test
    fun `Data is loaded from local IM database when available`() = runTest {
        val localData = listOf(sampleImWaifu.copy(7), sampleImWaifu.copy(8), sampleImWaifu.copy(9))
        val remoteData = listOf(sampleImWaifu.copy(4), sampleImWaifu.copy(5), sampleImWaifu.copy(6))

        val vm = buildModelWith(localData, remoteData)

        vm.onImReady(false, false, "waifu", false)


        vm.state.test {
            assertEquals(UiState(), awaitItem())
            assertEquals(UiState(waifus = localData, isLoading = false), awaitItem())
        }

    }



    private fun buildModelWith(localData:List<WaifuImItem> = emptyList(), remoteData: List<WaifuImItem> = emptyList()): WaifuImViewModel {
        val permissionChecker = FakePermissionChecker()
        val locationDataSource = FakeLocationDataSource()
        val regionRepository = RegionRepository(locationDataSource, permissionChecker)

        val favoriteDataSource = FakeFavoriteDataSource()
        val imLocalDataSource = FakeLocalImDataSource().apply { waifusIm.value = localData }
        val imRemoteDataSource = FakeRemoteImDataSource().apply { waifus = remoteData }
        val repo = WaifusImRepository(imLocalDataSource, favoriteDataSource ,imRemoteDataSource)

        // val Repository = buildRepositoryWith(localData, remoteData)

        val getWaifuImUseCase = GetWaifuImUseCase(repo)
        val requestWaifuImUseCase = RequestWaifuImUseCase(repo)
        val requestMoreWaifuImUseCase = RequestMoreWaifuImUseCase(repo)
        val clearWaifuImUseCase = ClearWaifuImUseCase(repo)
        val vm = WaifuImViewModel(getWaifuImUseCase, requestWaifuImUseCase, requestMoreWaifuImUseCase, clearWaifuImUseCase)
        return vm
    }

}