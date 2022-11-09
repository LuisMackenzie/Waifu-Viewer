package com.mackenzie.waifuviewer.ui.detail

import app.cash.turbine.test
import com.mackenzie.waifuviewer.data.db.WaifuBestDbItem
import com.mackenzie.waifuviewer.data.server.WaifuBestGif
import com.mackenzie.waifuviewer.data.server.WaifuBestPng
import com.mackenzie.waifuviewer.testrules.CoroutinesTestRule
import com.mackenzie.waifuviewer.ui.helpers.buildBestDatabaseWaifus
import com.mackenzie.waifuviewer.ui.helpers.buildBestRepositoryWith
import com.mackenzie.waifuviewer.usecases.best.FindWaifuBestUseCase
import com.mackenzie.waifuviewer.usecases.best.SwitchBestFavoriteUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class DetailBestIntegrationTests {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Test
    fun `UI is updated with BEST the Waifu on start`() = runTest {
        val vm = buildModelWith(id = 2, localData = buildBestDatabaseWaifus(1, 2, 3))

        vm.state.test {
            Assert.assertEquals(DetailBestViewModel.UiState(), awaitItem())
            Assert.assertEquals(2, awaitItem().waifu!!.id)
            cancel()
        }
    }

    @Test
    fun `Favorite BEST is updated in local data source`() = runTest {

        val vm = buildModelWith(5, localData = buildBestDatabaseWaifus(4, 5, 6))

        vm.onFavoriteClicked()


        vm.state.test {
            Assert.assertEquals(DetailBestViewModel.UiState(), awaitItem())
            Assert.assertEquals(false, awaitItem().waifu!!.isFavorite)
            Assert.assertEquals(true, awaitItem().waifu!!.isFavorite)
            cancel()
        }
    }

    private fun buildModelWith(
        id: Int,
        localData:List<WaifuBestDbItem> = emptyList(),
        remoteData: List<WaifuBestPng> = emptyList(),
        remoteDataGif: List<WaifuBestGif> = emptyList()
    ): DetailBestViewModel {
        /*val permissionChecker = FakePermissionChecker()
        val locationDataSource = FakeLocationDataSource()
        val regionRepository = RegionRepository(locationDataSource, permissionChecker)*/

        val repo = buildBestRepositoryWith(localData, remoteData, remoteDataGif)

        val findWaifuUseCase = FindWaifuBestUseCase(repo)
        val switchFavoriteUseCase = SwitchBestFavoriteUseCase(repo)
        val vm = DetailBestViewModel(id , findWaifuUseCase, switchFavoriteUseCase)
        return vm
    }

}