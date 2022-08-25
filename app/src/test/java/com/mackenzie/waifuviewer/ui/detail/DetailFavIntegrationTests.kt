package com.mackenzie.waifuviewer.ui.detail

import app.cash.turbine.test
import com.mackenzie.testshared.sampleFavWaifu
import com.mackenzie.waifuviewer.data.FavoritesRepository
import com.mackenzie.waifuviewer.domain.FavoriteItem
import com.mackenzie.waifuviewer.testrules.CoroutinesTestRule
import com.mackenzie.waifuviewer.ui.detail.DetailFavsViewModel.UiState
import com.mackenzie.waifuviewer.ui.fakes.FakeFavoriteDataSource
import com.mackenzie.waifuviewer.usecases.FindFavoriteUseCase
import com.mackenzie.waifuviewer.usecases.SwitchFavoriteUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class DetailFavIntegrationTests {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Test
    fun `UI is updated with the movie on start`() = runTest {
        val vm = buildModelWith(id = 2, localData = listOf(sampleFavWaifu.copy(1), sampleFavWaifu.copy(2)))

        vm.state.test {
            Assert.assertEquals(UiState(), awaitItem())
            Assert.assertEquals(UiState(waifu = sampleFavWaifu.copy(2)), awaitItem())
            cancel()
        }
    }

    @Test
    fun `Favorite IM is updated in local data source`() = runTest {
        val localData = listOf(sampleFavWaifu.copy(3), sampleFavWaifu.copy(4), sampleFavWaifu.copy(5))

        val vm = buildModelWith(4, localData = localData)

        vm.onFavoriteClicked()


        vm.state.test {
            Assert.assertEquals(UiState(), awaitItem())
            Assert.assertEquals(UiState(waifu = sampleFavWaifu.copy(id = 4, isFavorite = false)), awaitItem())
            Assert.assertEquals(UiState(waifu = sampleFavWaifu.copy(id = 4, isFavorite = true)), awaitItem())
            cancel()
        }
    }

    private fun buildModelWith(
        id: Int,
        localData:List<FavoriteItem> = emptyList(),
        remoteData: List<FavoriteItem> = emptyList()
    ): DetailFavsViewModel {

        val favoriteDataSource = FakeFavoriteDataSource().apply { favoriteWaifus.value = localData }
        val repo = FavoritesRepository(favoriteDataSource)

        // val Repository = buildRepositoryWith(localData, remoteData)

        val findFavoriteUseCase = FindFavoriteUseCase(repo)
        val switchFavoriteUseCase = SwitchFavoriteUseCase(repo)
        val vm = DetailFavsViewModel(id ,findFavoriteUseCase, switchFavoriteUseCase)
        return vm
    }

}