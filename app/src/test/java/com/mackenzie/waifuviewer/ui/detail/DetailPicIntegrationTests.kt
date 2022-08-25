package com.mackenzie.waifuviewer.ui.detail

import app.cash.turbine.test
import com.mackenzie.testshared.samplePicWaifu
import com.mackenzie.waifuviewer.data.RegionRepository
import com.mackenzie.waifuviewer.data.WaifusPicRepository
import com.mackenzie.waifuviewer.domain.WaifuPicItem
import com.mackenzie.waifuviewer.testrules.CoroutinesTestRule
import com.mackenzie.waifuviewer.ui.FakeLocationDataSource
import com.mackenzie.waifuviewer.ui.FakePermissionChecker
import com.mackenzie.waifuviewer.ui.detail.DetailPicsViewModel.UiState
import com.mackenzie.waifuviewer.ui.fakes.FakeFavoriteDataSource
import com.mackenzie.waifuviewer.ui.fakes.FakeLocalPicDataSource
import com.mackenzie.waifuviewer.ui.fakes.FakeRemotePicDataSource
import com.mackenzie.waifuviewer.usecases.FindWaifuPicUseCase
import com.mackenzie.waifuviewer.usecases.SwitchPicFavoriteUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class DetailPicIntegrationTests {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Test
    fun `UI is updated with the movie on start`() = runTest {
        val vm = buildModelWith(
            id = 2,
            localData = listOf(samplePicWaifu.copy(1), samplePicWaifu.copy(2))
        )

        vm.state.test {
            Assert.assertEquals(UiState(), awaitItem())
            Assert.assertEquals(UiState(waifuPic = samplePicWaifu.copy(2)), awaitItem())
            cancel()
        }
    }

    @Test
    fun `Favorite IM is updated in local data source`() = runTest {
        val localData = listOf(samplePicWaifu.copy(3), samplePicWaifu.copy(4), samplePicWaifu.copy(5))

        val vm = buildModelWith(4, localData = localData)

        vm.onFavoriteClicked()


        vm.state.test {
            Assert.assertEquals(UiState(), awaitItem())
            Assert.assertEquals(UiState(waifuPic = samplePicWaifu.copy(id = 4, isFavorite = false)), awaitItem())
            Assert.assertEquals(UiState(waifuPic = samplePicWaifu.copy(id = 4, isFavorite = true)), awaitItem())
            cancel()
        }
    }

    private fun buildModelWith(
        id: Int,
        localData:List<WaifuPicItem> = emptyList(),
        remoteData: List<WaifuPicItem> = emptyList()
    ): DetailPicsViewModel {

        val favoriteDataSource = FakeFavoriteDataSource()
        val picLocalDataSource = FakeLocalPicDataSource().apply { waifusPic.value = localData }
        val picRemoteDataSource = FakeRemotePicDataSource().apply { waifus = remoteData }
        val repo = WaifusPicRepository(picLocalDataSource, favoriteDataSource ,picRemoteDataSource)

        // val Repository = buildRepositoryWith(localData, remoteData)

        val findWaifuPicUseCase = FindWaifuPicUseCase(repo)
        val switchPicFavoriteUseCase = SwitchPicFavoriteUseCase(repo)
        val vm = DetailPicsViewModel(id , findWaifuPicUseCase, switchPicFavoriteUseCase)
        return vm
    }

}