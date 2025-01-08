package com.mackenzie.waifuviewer.ui.main

import app.cash.turbine.test
import com.mackenzie.waifuviewer.data.db.WaifuImDbItem
import com.mackenzie.waifuviewer.data.db.WaifuPicDbItem
import com.mackenzie.waifuviewer.data.server.models.WaifuIm
import com.mackenzie.waifuviewer.domain.ServerType
import com.mackenzie.waifuviewer.testrules.CoroutinesTestRule
import com.mackenzie.waifuviewer.ui.selector.SelectorViewModel
import com.mackenzie.waifuviewer.ui.selector.SelectorViewModel.UiState
import com.mackenzie.waifuviewer.ui.buildImDatabaseWaifus
import com.mackenzie.waifuviewer.ui.buildImRepositoryWith
import com.mackenzie.waifuviewer.ui.buildImRemoteWaifus
import com.mackenzie.waifuviewer.ui.buildPicRepositoryWith
import com.mackenzie.waifuviewer.ui.helpers.buildBestRepositoryWith
import com.mackenzie.waifuviewer.usecases.best.ClearWaifuBestUseCase
import com.mackenzie.waifuviewer.usecases.best.RequestOnlyWaifuBestUseCase
import com.mackenzie.waifuviewer.usecases.im.ClearWaifuImUseCase
import com.mackenzie.waifuviewer.usecases.im.GetWaifuImTagsUseCase
import com.mackenzie.waifuviewer.usecases.im.RequestOnlyWaifuImUseCase
import com.mackenzie.waifuviewer.usecases.im.RequestWaifuImTagsUseCase
import com.mackenzie.waifuviewer.usecases.pics.ClearWaifuPicUseCase
import com.mackenzie.waifuviewer.usecases.pics.RequestOnlyWaifuPicUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SelectorImIntegrationTests {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Test
    fun `Data is loaded from IM server when local source is empty`() = runTest {
        val remoteData = buildImRemoteWaifus(7)

        val vm = buildModelWith(remoteDataIm = remoteData)

        vm.loadErrorOrWaifu(serverType = ServerType.NORMAL)

        vm.state.test {
            assertEquals(UiState(), awaitItem())
            // Assert.assertEquals(UiState(waifus = emptyList()), awaitItem())
            // Assert.assertEquals(UiState(waifus = emptyList(), isLoading = true), awaitItem())
            // assertEquals(UiState(waifu = emptyList(), isLoading = false), awaitItem())
            val waifu = awaitItem().waifuIm!!
            assertEquals("Overview 7", waifu.source)

            cancel()
        }

    }

    @Test
    fun `Data is loaded from local IM database when available`() = runTest {
        val localData = buildImDatabaseWaifus(1, 2, 3)
        val remoteData = buildImRemoteWaifus(4, 5, 6)

        val vm = buildModelWith(localData, remoteData)

        vm.loadErrorOrWaifu(serverType = ServerType.NORMAL)


        vm.state.test {
            assertEquals(UiState(), awaitItem())
            val waifu = awaitItem().waifuIm!!
            assertEquals("Overview 4", waifu.source)
            cancel()
        }

    }

    /*@Test
    fun `Data is loaded from PICS server when local source is empty`() = runTest {
        val remoteData = buildPicRemoteWaifus(8)

        val vm = buildModelWith(remoteData = remoteData)

        vm.loadErrorOrWaifu(serverType = ServerType.ENHANCED)

        vm.state.test {
            assertEquals(UiState(), awaitItem())
            // Assert.assertEquals(UiState(waifus = emptyList()), awaitItem())
            // Assert.assertEquals(UiState(waifus = emptyList(), isLoading = true), awaitItem())
            // assertEquals(UiState(waifu = emptyList(), isLoading = false), awaitItem())
            val waifu = awaitItem().waifuPic!!
            assertEquals(0, waifu.id)

            cancel()
        }

    }*/

    /*@Test
    fun `Data is loaded from local PICS database when available`() = runTest {
        val localData = buildPicDatabaseWaifus(1, 2, 3)
        val remoteData = buildPicRemoteWaifus(4, 5, 6)

        val vm = buildModelWith(localData, remoteData)

        vm.loadErrorOrWaifu(serverType = ServerType.ENHANCED)


        vm.state.test {
            assertEquals(UiState(), awaitItem())
            val waifu = awaitItem().waifuPic!!
            assertEquals(0, waifu.id)
            cancel()
        }

    }*/

    private fun buildModelWith(
        localDataIm:List<WaifuImDbItem> = emptyList(),
        remoteDataIm: List<WaifuIm> = emptyList(),
        localDataPic:List<WaifuPicDbItem> = emptyList(),
        remoteDataPic: List<String> = emptyList()
        ): SelectorViewModel {

        /*val favoriteDataSource = FakeFavoriteDataSource()
        val imLocalDataSource = FakeLocalImDataSource().apply { waifusIm.value = localData }
        val imRemoteDataSource = FakeRemoteImDataSource().apply { waifus = remoteData }
        val repo = WaifusImRepository(imLocalDataSource, favoriteDataSource ,imRemoteDataSource)*/

        val repo = buildImRepositoryWith(localDataIm, remoteDataIm)
        val repoPic = buildPicRepositoryWith(localDataPic, remoteDataPic)
        val repoBest = buildBestRepositoryWith(emptyList(), emptyList(), emptyList())

        val getWaifuImTagsUseCase = GetWaifuImTagsUseCase(repo)
        val requestOnlyWaifuImUseCase = RequestOnlyWaifuImUseCase(repo)
        val requestOnlyWaifuPicUseCase = RequestOnlyWaifuPicUseCase(repoPic)
        val requestOnlyWaifuBestUseCase = RequestOnlyWaifuBestUseCase(repoBest)
        val requestWaifuImTagsUseCase = RequestWaifuImTagsUseCase(repo)
        val clearWaifuImUseCase = ClearWaifuImUseCase(repo)
        val clearWaifuPicUseCase = ClearWaifuPicUseCase(repoPic)
        val clearWaifuUseCase = ClearWaifuBestUseCase(repoBest)
        val vm = SelectorViewModel(
            getWaifuImTagsUseCase,
            requestOnlyWaifuImUseCase,
            requestOnlyWaifuPicUseCase,
            requestOnlyWaifuBestUseCase,
            requestWaifuImTagsUseCase,
            clearWaifuImUseCase,
            clearWaifuPicUseCase,
            clearWaifuUseCase
        )
        return vm
    }

}