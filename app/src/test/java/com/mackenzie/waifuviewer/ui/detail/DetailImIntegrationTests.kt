package com.mackenzie.waifuviewer.ui.detail

import app.cash.turbine.test
import com.mackenzie.testshared.sampleImWaifu
import com.mackenzie.waifuviewer.data.RegionRepository
import com.mackenzie.waifuviewer.data.WaifusImRepository
import com.mackenzie.waifuviewer.domain.WaifuImItem
import com.mackenzie.waifuviewer.testrules.CoroutinesTestRule
import com.mackenzie.waifuviewer.ui.FakeLocationDataSource
import com.mackenzie.waifuviewer.ui.FakePermissionChecker
import com.mackenzie.waifuviewer.ui.detail.DetailImViewModel.UiState
import com.mackenzie.waifuviewer.ui.fakes.FakeFavoriteDataSource
import com.mackenzie.waifuviewer.ui.fakes.FakeLocalImDataSource
import com.mackenzie.waifuviewer.ui.fakes.FakeRemoteImDataSource
import com.mackenzie.waifuviewer.usecases.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class DetailImIntegrationTests {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Test
    fun `UI is updated with the movie on start`() = runTest {
        val vm = buildModelWith(
            id = 2,
            localData = listOf(sampleImWaifu.copy(1), sampleImWaifu.copy(2))
        )

        vm.state.test {
            assertEquals(UiState(), awaitItem())
            assertEquals(UiState(waifuIm = sampleImWaifu.copy(2)), awaitItem())
            cancel()
        }
    }

    @Test
    fun `Favorite IM is updated in local data source`() = runTest {
        val localData = listOf(sampleImWaifu.copy(3), sampleImWaifu.copy(4), sampleImWaifu.copy(5))

        val vm = buildModelWith(4, localData = localData)

        vm.onFavoriteClicked()


        vm.state.test {
            assertEquals(UiState(), awaitItem())
            assertEquals(UiState(waifuIm = sampleImWaifu.copy(id = 4, isFavorite = false)), awaitItem())
            assertEquals(UiState(waifuIm = sampleImWaifu.copy(id = 4, isFavorite = true)), awaitItem())
            cancel()
        }
    }

    private fun buildModelWith(
        id: Int,
        localData:List<WaifuImItem> = emptyList(),
        remoteData: List<WaifuImItem> = emptyList()
    ): DetailImViewModel {
        /*val permissionChecker = FakePermissionChecker()
        val locationDataSource = FakeLocationDataSource()
        val regionRepository = RegionRepository(locationDataSource, permissionChecker)*/

        val favoriteDataSource = FakeFavoriteDataSource()
        val imLocalDataSource = FakeLocalImDataSource().apply { waifusIm.value = localData }
        val imRemoteDataSource = FakeRemoteImDataSource().apply { waifus = remoteData }
        val repo = WaifusImRepository(imLocalDataSource, favoriteDataSource ,imRemoteDataSource)

        // val Repository = buildRepositoryWith(localData, remoteData)

        val findWaifuImUseCase = FindWaifuImUseCase(repo)
        val switchImFavoriteUseCase = SwitchImFavoriteUseCase(repo)
        val vm = DetailImViewModel(id , findWaifuImUseCase, switchImFavoriteUseCase)
        return vm
    }

}