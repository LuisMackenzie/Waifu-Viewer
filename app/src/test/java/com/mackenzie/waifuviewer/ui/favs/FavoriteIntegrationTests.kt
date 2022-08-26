package com.mackenzie.waifuviewer.ui.favs

import app.cash.turbine.test
import com.mackenzie.waifuviewer.data.db.FavoriteDbItem
import com.mackenzie.waifuviewer.testrules.CoroutinesTestRule
import com.mackenzie.waifuviewer.ui.*
import com.mackenzie.waifuviewer.ui.favs.FavoriteViewModel.UiState
import com.mackenzie.waifuviewer.usecases.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class FavoriteIntegrationTests {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Test
    fun `Data is loaded from local IM database when available`() = runTest {
        val localData = buildFavDatabaseWaifus(1, 2, 3)

        val vm = buildModelWith(localData)

        // vm.onImReady(false, false, "waifu", false)
        vm.initScope()

        vm.state.test {
            Assert.assertEquals(UiState(), awaitItem())
            val waifus = awaitItem().waifus!!
            Assert.assertEquals("Overview 1", waifus[0].title)
            Assert.assertEquals("Overview 2", waifus[1].title)
            Assert.assertEquals("Overview 3", waifus[2].title)
            cancel()
        }

    }



    private fun buildModelWith(localData:List<FavoriteDbItem> = emptyList()): FavoriteViewModel {

        val repo = buildFavRepositoryWith(localData)

        val getFavoritesUseCase = GetFavoritesUseCase(repo)
        val deleteFavoriteUseCase = DeleteFavoriteUseCase(repo)
        val vm = FavoriteViewModel(getFavoritesUseCase, deleteFavoriteUseCase)
        return vm
    }

}