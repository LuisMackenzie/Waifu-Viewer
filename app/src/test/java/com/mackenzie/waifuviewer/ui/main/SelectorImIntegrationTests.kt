package com.mackenzie.waifuviewer.ui.main

import app.cash.turbine.test
import com.mackenzie.waifuviewer.data.db.WaifuImDbItem
import com.mackenzie.waifuviewer.data.server.models.WaifuIm
import com.mackenzie.waifuviewer.testrules.CoroutinesTestRule
import com.mackenzie.waifuviewer.ui.buildImDatabaseWaifus
import com.mackenzie.waifuviewer.ui.buildImRepositoryWith
import com.mackenzie.waifuviewer.ui.buildImRemoteWaifus
import com.mackenzie.waifuviewer.ui.main.SelectorImViewModel.UiState
import com.mackenzie.waifuviewer.usecases.im.GetWaifuImTagsUseCase
import com.mackenzie.waifuviewer.usecases.im.RequestOnlyWaifuImUseCase
import com.mackenzie.waifuviewer.usecases.im.RequestWaifuImTagsUseCase
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
        val remoteData = buildImRemoteWaifus(7)

        val vm = buildModelWith(remoteData = remoteData)

        vm.loadErrorOrWaifu()

        vm.state.test {
            assertEquals(UiState(), awaitItem())
            // Assert.assertEquals(UiState(waifus = emptyList()), awaitItem())
            // Assert.assertEquals(UiState(waifus = emptyList(), isLoading = true), awaitItem())
            // assertEquals(UiState(waifu = emptyList(), isLoading = false), awaitItem())
            val waifu = awaitItem().waifu!!
            assertEquals("Overview 7", waifu.source)

            cancel()
        }

    }

    @Test
    fun `Data is loaded from local IM database when available`() = runTest {
        val localData = buildImDatabaseWaifus(1, 2, 3)
        val remoteData = buildImRemoteWaifus(4, 5, 6)

        val vm = buildModelWith(localData, remoteData)

        vm.loadErrorOrWaifu()


        vm.state.test {
            assertEquals(UiState(), awaitItem())
            val waifu = awaitItem().waifu!!
            assertEquals("Overview 4", waifu.source)
            cancel()
        }

    }

    private fun buildModelWith(localData:List<WaifuImDbItem> = emptyList(), remoteData: List<WaifuIm> = emptyList()): SelectorImViewModel {

        /*val favoriteDataSource = FakeFavoriteDataSource()
        val imLocalDataSource = FakeLocalImDataSource().apply { waifusIm.value = localData }
        val imRemoteDataSource = FakeRemoteImDataSource().apply { waifus = remoteData }
        val repo = WaifusImRepository(imLocalDataSource, favoriteDataSource ,imRemoteDataSource)*/

        val repo = buildImRepositoryWith(localData, remoteData)

        val getWaifuImTagsUseCase = GetWaifuImTagsUseCase(repo)
        val requestOnlyWaifuImUseCase = RequestOnlyWaifuImUseCase(repo)
        val requestWaifuImTagsUseCase = RequestWaifuImTagsUseCase(repo)
        val vm = SelectorImViewModel(getWaifuImTagsUseCase, requestOnlyWaifuImUseCase, requestWaifuImTagsUseCase)
        return vm
    }

}