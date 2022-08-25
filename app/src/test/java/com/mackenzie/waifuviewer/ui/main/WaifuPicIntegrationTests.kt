package com.mackenzie.waifuviewer.ui.main

import app.cash.turbine.test
import com.mackenzie.testshared.samplePicWaifu
import com.mackenzie.waifuviewer.WaifuPicsViewModel
import com.mackenzie.waifuviewer.WaifuPicsViewModel.UiState
import com.mackenzie.waifuviewer.data.RegionRepository
import com.mackenzie.waifuviewer.data.WaifusPicRepository
import com.mackenzie.waifuviewer.domain.WaifuPicItem
import com.mackenzie.waifuviewer.testrules.CoroutinesTestRule
import com.mackenzie.waifuviewer.ui.FakeLocationDataSource
import com.mackenzie.waifuviewer.ui.FakePermissionChecker
import com.mackenzie.waifuviewer.ui.fakes.*
import com.mackenzie.waifuviewer.usecases.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class WaifuPicIntegrationTests {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Test
    fun `Data is loaded from PICS server when local source is empty`() = runTest {
        val remoteData = listOf(samplePicWaifu.copy(1), samplePicWaifu.copy(2), samplePicWaifu.copy(3))

        val vm = buildModelWith(remoteData = remoteData)

        vm.onPicsReady(false, "waifu")


        vm.state.test {
            Assert.assertEquals(UiState(), awaitItem())
            // Assert.assertEquals(UiState(waifus = emptyList()), awaitItem())
            // Assert.assertEquals(UiState(waifus = emptyList(), isLoading = true), awaitItem())
            Assert.assertEquals(UiState(waifus = emptyList(), isLoading = false), awaitItem())
            Assert.assertEquals(UiState(waifus = remoteData), awaitItem())
        }

    }

    @Test
    fun `Data is loaded from local IM database when available`() = runTest {
        val localData = listOf(samplePicWaifu.copy(7), samplePicWaifu.copy(8), samplePicWaifu.copy(9))
        val remoteData = listOf(samplePicWaifu.copy(4), samplePicWaifu.copy(5), samplePicWaifu.copy(6))

        val vm = buildModelWith(localData, remoteData)

        vm.onPicsReady(false, "waifu")


        vm.state.test {
            Assert.assertEquals(UiState(), awaitItem())
            Assert.assertEquals(UiState(waifus = localData, isLoading = false), awaitItem())
        }

    }

    private fun buildModelWith(localData:List<WaifuPicItem> = emptyList(), remoteData: List<WaifuPicItem> = emptyList()): WaifuPicsViewModel {
        val permissionChecker = FakePermissionChecker()
        val locationDataSource = FakeLocationDataSource()
        val regionRepository = RegionRepository(locationDataSource, permissionChecker)

        val favoriteDataSource = FakeFavoriteDataSource()
        val picLocalDataSource = FakeLocalPicDataSource().apply { waifusPic.value = localData }
        val picRemoteDataSource = FakeRemotePicDataSource().apply { waifus = remoteData }
        val repo = WaifusPicRepository(picLocalDataSource, favoriteDataSource ,picRemoteDataSource)

        // val Repository = buildRepositoryWith(localData, remoteData)

        val getWaifuPicUseCase = GetWaifuPicUseCase(repo)
        val requestWaifuPicUseCase = RequestWaifuPicUseCase(repo)
        val requestMoreWaifuPicUseCase = RequestMoreWaifuPicUseCase(repo)
        val clearWaifuPicUseCase = ClearWaifuPicUseCase(repo)
        val vm = WaifuPicsViewModel(getWaifuPicUseCase, requestWaifuPicUseCase, requestMoreWaifuPicUseCase, clearWaifuPicUseCase)
        return vm
    }

}