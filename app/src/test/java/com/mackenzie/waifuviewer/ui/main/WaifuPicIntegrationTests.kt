package com.mackenzie.waifuviewer.ui.main

import app.cash.turbine.test
import com.mackenzie.waifuviewer.WaifuPicsViewModel
import com.mackenzie.waifuviewer.WaifuPicsViewModel.UiState
import com.mackenzie.waifuviewer.data.db.WaifuPicDbItem
import com.mackenzie.waifuviewer.testrules.CoroutinesTestRule
import com.mackenzie.waifuviewer.ui.*
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
        val remoteData = buildPicRemoteWaifus(4, 5, 6)

        val vm = buildModelWith(remoteData = remoteData)

        vm.onPicsReady(false, "waifu")


        vm.state.test {
            Assert.assertEquals(UiState(), awaitItem())
            // Assert.assertEquals(UiState(waifus = emptyList()), awaitItem())
            // Assert.assertEquals(UiState(waifus = emptyList(), isLoading = true), awaitItem())
            Assert.assertEquals(UiState(waifus = emptyList(), isLoading = false), awaitItem())
            val waifus = awaitItem().waifus!!
            Assert.assertEquals("4", waifus[0].id)
            Assert.assertEquals("5", waifus[1].id)
            Assert.assertEquals("6", waifus[2].id)

            cancel()
        }

    }

    @Test
    fun `Data is loaded from local IM database when available`() = runTest {
        val localData = buildPicDatabaseWaifus(1, 2, 3)
        val remoteData = buildPicRemoteWaifus(4, 5, 6)

        val vm = buildModelWith(localData, remoteData)

        vm.onPicsReady(false, "waifu")


        vm.state.test {
            Assert.assertEquals(UiState(), awaitItem())
            val waifus = awaitItem().waifus!!
            Assert.assertEquals("1", waifus[0].id)
            Assert.assertEquals("2", waifus[1].id)
            Assert.assertEquals("3", waifus[2].id)

            cancel()
        }

    }

    private fun buildModelWith(localData:List<WaifuPicDbItem> = emptyList(), remoteData: List<String> = emptyList()): WaifuPicsViewModel {


        val repo = buildPicRepositoryWith(localData, remoteData)

        val getWaifuPicUseCase = GetWaifuPicUseCase(repo)
        val requestWaifuPicUseCase = RequestWaifuPicUseCase(repo)
        val requestMoreWaifuPicUseCase = RequestMoreWaifuPicUseCase(repo)
        val clearWaifuPicUseCase = ClearWaifuPicUseCase(repo)
        val vm = WaifuPicsViewModel(getWaifuPicUseCase, requestWaifuPicUseCase, requestMoreWaifuPicUseCase, clearWaifuPicUseCase)
        return vm
    }

}