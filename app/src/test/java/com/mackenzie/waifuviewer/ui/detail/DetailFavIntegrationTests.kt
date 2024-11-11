package com.mackenzie.waifuviewer.ui.detail

import app.cash.turbine.test
import com.mackenzie.waifuviewer.data.TraceMoeRepository
import com.mackenzie.waifuviewer.data.db.FavoriteDbItem
import com.mackenzie.waifuviewer.data.server.models.RemoteConnect
import com.mackenzie.waifuviewer.data.server.ServerMoeDataSource
import com.mackenzie.waifuviewer.testrules.CoroutinesTestRule
import com.mackenzie.waifuviewer.ui.buildFavDatabaseWaifus
import com.mackenzie.waifuviewer.ui.buildFavRepositoryWith
import com.mackenzie.waifuviewer.ui.detail.DetailFavsViewModel.UiState
import com.mackenzie.waifuviewer.ui.fakes.FakeRemoteBestService
import com.mackenzie.waifuviewer.ui.fakes.FakeRemoteImService
import com.mackenzie.waifuviewer.ui.fakes.FakeRemoteMoeService
import com.mackenzie.waifuviewer.ui.fakes.FakeRemotePicsService
import com.mackenzie.waifuviewer.usecases.FindFavoriteUseCase
import com.mackenzie.waifuviewer.usecases.SwitchFavoriteUseCase
import com.mackenzie.waifuviewer.usecases.moe.GetSearchMoeUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class DetailFavIntegrationTests {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Test
    fun `UI is updated with the Waifu on start`() = runTest {
        val vm = buildModelWith(id = 2, localData = buildFavDatabaseWaifus(1, 2, 3))

        vm.state.test {
            assertEquals(UiState(), awaitItem())
            assertEquals(2, awaitItem().waifu!!.id)
            cancel()
        }
    }

    @Test
    fun `Favorite IM is updated in local data source`() = runTest {

        val vm = buildModelWith(5, localData = buildFavDatabaseWaifus(4, 5, 6))

        vm.onFavoriteClicked()


        vm.state.test {
            assertEquals(UiState(), awaitItem())
            assertEquals(false, awaitItem().waifu!!.isFavorite)
            assertEquals(true, awaitItem().waifu!!.isFavorite)
            cancel()
        }
    }

    private fun buildModelWith(
        id: Int,
        localData:List<FavoriteDbItem> = emptyList()
    ): DetailFavsViewModel {

        //  val favoriteDataSource = FakeFavoriteDataSource().apply { favoriteWaifus.value = localData }
        // val repo = FavoritesRepository(favoriteDataSource)

        // val Repository = buildRepositoryWith(localData, remoteData)
        val repo = buildFavRepositoryWith(localData)

        val findFavoriteUseCase = FindFavoriteUseCase(repo)
        val switchFavoriteUseCase = SwitchFavoriteUseCase(repo)
        val getSearchMoeUseCase = GetSearchMoeUseCase(repo = TraceMoeRepository(
            ServerMoeDataSource(
            RemoteConnect(FakeRemoteImService(), FakeRemotePicsService(), FakeRemoteBestService(), FakeRemoteMoeService())
            )
        )
        )
        val vm = DetailFavsViewModel(id ,findFavoriteUseCase, switchFavoriteUseCase, getSearchMoeUseCase)
        return vm
    }

}