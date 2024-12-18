package com.mackenzie.waifuviewer.ui.detail

import app.cash.turbine.test
import com.mackenzie.waifuviewer.data.TraceMoeRepository
import com.mackenzie.waifuviewer.data.db.WaifuImDbItem
import com.mackenzie.waifuviewer.data.server.models.RemoteConnect
import com.mackenzie.waifuviewer.data.server.ServerMoeDataSource
import com.mackenzie.waifuviewer.data.server.models.WaifuIm
import com.mackenzie.waifuviewer.testrules.CoroutinesTestRule
import com.mackenzie.waifuviewer.ui.buildImDatabaseWaifus
import com.mackenzie.waifuviewer.ui.buildImRepositoryWith
import com.mackenzie.waifuviewer.ui.detail.DetailImViewModel.UiState
import com.mackenzie.waifuviewer.ui.fakes.FakeRemoteBestService
import com.mackenzie.waifuviewer.ui.fakes.FakeRemoteImService
import com.mackenzie.waifuviewer.ui.FakeRemoteMoeService
import com.mackenzie.waifuviewer.ui.FakeRemoteOpenAiService
import com.mackenzie.waifuviewer.ui.fakes.FakeRemotePicsService
import com.mackenzie.waifuviewer.usecases.im.FindWaifuImUseCase
import com.mackenzie.waifuviewer.usecases.im.SwitchImFavoriteUseCase
import com.mackenzie.waifuviewer.usecases.moe.GetSearchMoeUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any

@ExperimentalCoroutinesApi
class DetailImIntegrationTests {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Test
    fun `UI is updated with IM the Waifu on start`() = runTest {
        val vm = buildModelWith(id = 2, localData = buildImDatabaseWaifus(1, 2, 3))

        vm.state.test {
            assertEquals(UiState(), awaitItem())
            assertEquals(2, awaitItem().waifuIm!!.id)
            cancel()
        }
    }

    @Test
    fun `Favorite IM is updated in local data source`() = runTest {

        val vm = buildModelWith(5, localData = buildImDatabaseWaifus(4, 5, 6))

        vm.onFavoriteClicked()


        vm.state.test {
            assertEquals(UiState(), awaitItem())
            assertEquals(false, awaitItem().waifuIm!!.isFavorite)
            // assertEquals(true, awaitItem().waifuIm!!.isFavorite)
            cancel()
        }
    }

    private fun buildModelWith(
        id: Int,
        localData:List<WaifuImDbItem> = emptyList(),
        remoteData: List<WaifuIm> = emptyList()
    ): DetailImViewModel {
        /*val permissionChecker = FakePermissionChecker()
        val locationDataSource = FakeLocationDataSource()
        val regionRepository = RegionRepository(locationDataSource, permissionChecker)*/

        val repo = buildImRepositoryWith(localData, remoteData)

        val findWaifuImUseCase = FindWaifuImUseCase(repo)
        val switchImFavoriteUseCase = SwitchImFavoriteUseCase(repo)
        val getSearchMoeUseCase = GetSearchMoeUseCase(repo = TraceMoeRepository(
            ServerMoeDataSource(
                RemoteConnect(
                    FakeRemoteImService(),
                    FakeRemotePicsService(),
                    FakeRemoteBestService(),
                    FakeRemoteMoeService(),
                    FakeRemoteOpenAiService()
                )            )
        )
        )
        val vm = DetailImViewModel(id , findWaifuImUseCase, switchImFavoriteUseCase, getSearchMoeUseCase)
        return vm
    }

}