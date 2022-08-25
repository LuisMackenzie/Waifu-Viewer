package com.mackenzie.waifuviewer.ui.main

import app.cash.turbine.test
import com.mackenzie.testshared.sampleImWaifu
import com.mackenzie.waifuviewer.data.WaifusImRepository
import com.mackenzie.waifuviewer.domain.WaifuImItem
import com.mackenzie.waifuviewer.testrules.CoroutinesTestRule
import com.mackenzie.waifuviewer.ui.fakes.FakeFavoriteDataSource
import com.mackenzie.waifuviewer.ui.fakes.FakeLocalImDataSource
import com.mackenzie.waifuviewer.ui.fakes.FakeRemoteImDataSource
import com.mackenzie.waifuviewer.ui.main.SelectorImViewModel.UiState
import com.mackenzie.waifuviewer.usecases.RequestOnlyWaifuImUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SelectorImIntegrationTests {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Test
    fun `Data is loaded from IM server when local source is empty`() = runTest {
        val remoteData = sampleImWaifu.copy(1)

        val vm = buildModelWith(remoteData = listOf(remoteData))

        vm.loadErrorOrWaifu()

        vm.state.test {
            assertEquals(UiState(), awaitItem())
            // Assert.assertEquals(UiState(waifus = emptyList()), awaitItem())
            // Assert.assertEquals(UiState(waifus = emptyList(), isLoading = true), awaitItem())
            // assertEquals(UiState(waifu = emptyList(), isLoading = false), awaitItem())
            assertEquals(UiState(waifu = remoteData), awaitItem())
        }

    }

    @Test
    fun `Data is loaded from local IM database when available`() = runTest {
        val localData = sampleImWaifu.copy(7)
        val remoteData = listOf(sampleImWaifu.copy(4), sampleImWaifu.copy(5), sampleImWaifu.copy(6))

        val vm = buildModelWith(listOf(localData), remoteData)

        vm.loadErrorOrWaifu()


        vm.state.test {
            assertEquals(UiState(), awaitItem())
            assertEquals(UiState(waifu = remoteData[0]), awaitItem())
        }

    }

    private fun buildModelWith(localData:List<WaifuImItem> = emptyList(), remoteData: List<WaifuImItem> = emptyList()): SelectorImViewModel {

        val favoriteDataSource = FakeFavoriteDataSource()
        val imLocalDataSource = FakeLocalImDataSource().apply { waifusIm.value = localData }
        val imRemoteDataSource = FakeRemoteImDataSource().apply { waifus = remoteData }
        val repo = WaifusImRepository(imLocalDataSource, favoriteDataSource ,imRemoteDataSource)

        // val Repository = buildRepositoryWith(localData, remoteData)

        val requestOnlyWaifuImUseCase = RequestOnlyWaifuImUseCase(repo)
        val vm = SelectorImViewModel(requestOnlyWaifuImUseCase)
        return vm
    }

}