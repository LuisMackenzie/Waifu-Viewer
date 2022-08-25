package com.mackenzie.waifuviewer.ui.main

import app.cash.turbine.test
import com.mackenzie.testshared.samplePicWaifu
import com.mackenzie.waifuviewer.data.WaifusPicRepository
import com.mackenzie.waifuviewer.domain.WaifuPicItem
import com.mackenzie.waifuviewer.testrules.CoroutinesTestRule
import com.mackenzie.waifuviewer.ui.SelectorPicViewModel
import com.mackenzie.waifuviewer.ui.SelectorPicViewModel.UiState
import com.mackenzie.waifuviewer.ui.fakes.FakeFavoriteDataSource
import com.mackenzie.waifuviewer.ui.fakes.FakeLocalPicDataSource
import com.mackenzie.waifuviewer.ui.fakes.FakeRemotePicDataSource
import com.mackenzie.waifuviewer.usecases.RequestOnlyWaifuPicUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SelectorPicIntegrationTests {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Test
    fun `Data is loaded from IM server when local source is empty`() = runTest {
        val remoteData = samplePicWaifu.copy(1)

        val vm = buildModelWith(remoteData = listOf(remoteData))

        vm.loadErrorOrWaifu()

        vm.state.test {
            Assert.assertEquals(UiState(), awaitItem())
            // Assert.assertEquals(UiState(waifus = emptyList()), awaitItem())
            // Assert.assertEquals(UiState(waifus = emptyList(), isLoading = true), awaitItem())
            // assertEquals(UiState(waifu = emptyList(), isLoading = false), awaitItem())
            Assert.assertEquals(UiState(waifu = remoteData), awaitItem())
        }

    }

    @Test
    fun `Data is loaded from local IM database when available`() = runTest {
        val localData = samplePicWaifu.copy(7)
        val remoteData = listOf(samplePicWaifu.copy(4), samplePicWaifu.copy(5), samplePicWaifu.copy(6))

        val vm = buildModelWith(listOf(localData), remoteData)

        vm.loadErrorOrWaifu()


        vm.state.test {
            Assert.assertEquals(UiState(), awaitItem())
            Assert.assertEquals(UiState(waifu = remoteData[0]), awaitItem())
        }

    }

    private fun buildModelWith(localData:List<WaifuPicItem> = emptyList(), remoteData: List<WaifuPicItem> = emptyList()): SelectorPicViewModel {

        val favoriteDataSource = FakeFavoriteDataSource()
        val picLocalDataSource = FakeLocalPicDataSource().apply { waifusPic.value = localData }
        val picRemoteDataSource = FakeRemotePicDataSource().apply { waifus = remoteData }
        val repo = WaifusPicRepository(picLocalDataSource, favoriteDataSource ,picRemoteDataSource)

        // val Repository = buildRepositoryWith(localData, remoteData)

        val requestOnlyWaifuPicUseCase = RequestOnlyWaifuPicUseCase(repo)
        val vm = SelectorPicViewModel(requestOnlyWaifuPicUseCase)
        return vm
    }

}