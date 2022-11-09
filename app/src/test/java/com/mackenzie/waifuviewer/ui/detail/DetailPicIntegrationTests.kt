package com.mackenzie.waifuviewer.ui.detail

import app.cash.turbine.test
import com.mackenzie.waifuviewer.data.db.WaifuPicDbItem
import com.mackenzie.waifuviewer.testrules.CoroutinesTestRule
import com.mackenzie.waifuviewer.ui.buildPicDatabaseWaifus
import com.mackenzie.waifuviewer.ui.buildPicRepositoryWith
import com.mackenzie.waifuviewer.ui.detail.DetailPicsViewModel.UiState
import com.mackenzie.waifuviewer.usecases.pics.FindWaifuPicUseCase
import com.mackenzie.waifuviewer.usecases.pics.SwitchPicFavoriteUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class DetailPicIntegrationTests {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Test
    fun `UI is updated with PICS the Waifu on start`() = runTest {
        val vm = buildModelWith(id = 2, localData = buildPicDatabaseWaifus(1, 2, 3))

        vm.state.test {
            assertEquals(UiState(), awaitItem())
            assertEquals(2, awaitItem().waifuPic!!.id)
            cancel()
        }
    }

    @Test
    fun `Favorite PICS is updated in local data source`() = runTest {

        val vm = buildModelWith(5, localData = buildPicDatabaseWaifus(4, 5, 6))

        vm.onFavoriteClicked()


        vm.state.test {
            assertEquals(UiState(), awaitItem())
            assertEquals(false, awaitItem().waifuPic!!.isFavorite)
            assertEquals(true, awaitItem().waifuPic!!.isFavorite)
            cancel()
        }
    }

    private fun buildModelWith(
        id: Int,
        localData:List<WaifuPicDbItem> = emptyList(),
        remoteData: List<String> = emptyList()
    ): DetailPicsViewModel {

        val repo = buildPicRepositoryWith(localData, remoteData)

        val findWaifuPicUseCase = FindWaifuPicUseCase(repo)
        val switchPicFavoriteUseCase = SwitchPicFavoriteUseCase(repo)
        val vm = DetailPicsViewModel(id , findWaifuPicUseCase, switchPicFavoriteUseCase)
        return vm
    }

}