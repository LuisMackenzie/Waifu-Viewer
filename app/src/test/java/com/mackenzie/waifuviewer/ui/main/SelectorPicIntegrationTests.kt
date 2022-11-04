package com.mackenzie.waifuviewer.ui.main

import app.cash.turbine.test
import com.mackenzie.waifuviewer.data.db.WaifuPicDbItem
import com.mackenzie.waifuviewer.testrules.CoroutinesTestRule
import com.mackenzie.waifuviewer.ui.SelectorPicViewModel
import com.mackenzie.waifuviewer.ui.SelectorPicViewModel.UiState
import com.mackenzie.waifuviewer.ui.buildPicDatabaseWaifus
import com.mackenzie.waifuviewer.ui.buildPicRepositoryWith
import com.mackenzie.waifuviewer.ui.buildPicRemoteWaifus
import com.mackenzie.waifuviewer.usecases.pics.RequestOnlyWaifuPicUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SelectorPicIntegrationTests {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Test
    fun `Data is loaded from IM server when local source is empty`() = runTest {
        val remoteData = buildPicRemoteWaifus(8)

        val vm = buildModelWith(remoteData = remoteData)

        vm.loadErrorOrWaifu()

        vm.state.test {
            Assert.assertEquals(UiState(), awaitItem())
            // Assert.assertEquals(UiState(waifus = emptyList()), awaitItem())
            // Assert.assertEquals(UiState(waifus = emptyList(), isLoading = true), awaitItem())
            // assertEquals(UiState(waifu = emptyList(), isLoading = false), awaitItem())
            val waifu = awaitItem().waifu!!
            assertEquals(0, waifu.id)

            cancel()
        }

    }

    @Test
    fun `Data is loaded from local IM database when available`() = runTest {
        val localData = buildPicDatabaseWaifus(1, 2, 3)
        val remoteData = buildPicRemoteWaifus(4, 5, 6)

        val vm = buildModelWith(localData, remoteData)

        vm.loadErrorOrWaifu()


        vm.state.test {
            assertEquals(UiState(), awaitItem())
            val waifu = awaitItem().waifu!!
            assertEquals(0, waifu.id)
            cancel()
        }

    }

    private fun buildModelWith(localData:List<WaifuPicDbItem> = emptyList(), remoteData: List<String> = emptyList()): SelectorPicViewModel {

        /*val favoriteDataSource = FakeFavoriteDataSource()
        val picLocalDataSource = FakeLocalPicDataSource().apply { waifusPic.value = localData }
        val picRemoteDataSource = FakeRemotePicDataSource().apply { waifus = remoteData }
        val repo = WaifusPicRepository(picLocalDataSource, favoriteDataSource ,picRemoteDataSource)*/

        // val Repository = buildRepositoryWith(localData, remoteData)
        val repo = buildPicRepositoryWith(localData, remoteData)

        val requestOnlyWaifuPicUseCase = RequestOnlyWaifuPicUseCase(repo)
        val vm = SelectorPicViewModel(requestOnlyWaifuPicUseCase)
        return vm
    }

}