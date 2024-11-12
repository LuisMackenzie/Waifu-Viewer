package com.mackenzie.waifuviewer.ui.detail

import app.cash.turbine.test
import com.mackenzie.waifuviewer.data.TraceMoeRepository
import com.mackenzie.waifuviewer.data.db.WaifuBestDbItem
import com.mackenzie.waifuviewer.data.server.models.RemoteConnect
import com.mackenzie.waifuviewer.data.server.ServerMoeDataSource
import com.mackenzie.waifuviewer.data.server.models.WaifuBestGif
import com.mackenzie.waifuviewer.data.server.models.WaifuBestPng
import com.mackenzie.waifuviewer.testrules.CoroutinesTestRule
import com.mackenzie.waifuviewer.ui.FakeRemoteMoeService
import com.mackenzie.waifuviewer.ui.FakeRemoteOpenAiService
import com.mackenzie.waifuviewer.ui.helpers.buildBestDatabaseWaifus
import com.mackenzie.waifuviewer.ui.helpers.buildBestRepositoryWith
import com.mackenzie.waifuviewer.ui.detail.DetailBestViewModel.UiState
import com.mackenzie.waifuviewer.ui.fakes.FakeRemoteBestService
import com.mackenzie.waifuviewer.ui.fakes.FakeRemoteImService
import com.mackenzie.waifuviewer.ui.fakes.FakeRemotePicsService
import com.mackenzie.waifuviewer.usecases.best.FindWaifuBestUseCase
import com.mackenzie.waifuviewer.usecases.best.SwitchBestFavoriteUseCase
import com.mackenzie.waifuviewer.usecases.moe.GetSearchMoeUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any

@ExperimentalCoroutinesApi
class DetailBestIntegrationTests {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Test
    fun `UI is updated with BEST the Waifu on start`() = runTest {
        val vm = buildModelWith(id = 2, localData = buildBestDatabaseWaifus(1, 2, 3))

        vm.state.test {
            Assert.assertEquals(UiState(), awaitItem())
            Assert.assertEquals(2, awaitItem().waifu!!.id)
            cancel()
        }
    }

    @Test
    fun `Favorite BEST is updated in local data source`() = runTest {

        val vm = buildModelWith(5, localData = buildBestDatabaseWaifus(4, 5, 6))

        vm.onFavoriteClicked()


        vm.state.test {
            Assert.assertEquals(UiState(), awaitItem())
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
        val getSearchMoeUseCase = GetSearchMoeUseCase(repo = TraceMoeRepository(ServerMoeDataSource(
            RemoteConnect(
                FakeRemoteImService(),
                FakeRemotePicsService(),
                FakeRemoteBestService(),
                FakeRemoteMoeService(),
                FakeRemoteOpenAiService()
            )
        )
        )
        )
        val vm = DetailBestViewModel(id , findWaifuUseCase, switchFavoriteUseCase, getSearchMoeUseCase)
        return vm
    }

}