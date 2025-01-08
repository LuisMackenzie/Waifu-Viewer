package com.mackenzie.waifuviewer.ui.main

import android.Manifest
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.domain.*
import com.mackenzie.waifuviewer.domain.im.WaifuImItem
import com.mackenzie.waifuviewer.ui.selector.SelectorFragmentDirections
import com.mackenzie.waifuviewer.ui.common.PermissionRequester
import com.mackenzie.waifuviewer.ui.favs.FavoriteFragmentDirections
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

typealias ImListener = (WaifuImItem) -> Unit
typealias PicListener = (WaifuPicItem) -> Unit
typealias BestListener = (WaifuBestItem) -> Unit

class MainState(
    private val context: Context,
    private val scope: CoroutineScope,
    private val navController: NavController,
    private val permissionRequester: PermissionRequester
) {
    /*fun onWaifuImClicked(waifu: WaifuImItem) {
        val action = WaifuFragmentDirections.actionWaifuImToDetail(waifu.id)
        navController.navigate(action)
    }*/

    /*fun onWaifuPicsClicked(waifu: WaifuPicItem) {
        val action = WaifuFragmentDirections.actionWaifuPicsToDetail(waifu.id)
        navController.navigate(action)
    }*/

    /*fun onWaifuBestClicked(waifu: WaifuBestItem) {
        val action = WaifuFragmentDirections.actionWaifuBestToDetail(waifu.id)
        navController.navigate(action)
    }*/

    /*fun onWaifuFavoriteClicked(waifu: FavoriteItem) {
        val action = FavoriteFragmentDirections.actionFavoritesToDetail(waifu.id)
        navController.navigate(action)
    }*/

    /*fun onButtonGetWaifuClicked(bun: Bundle) {
        val action = SelectorFragmentDirections.actionSelectorToWaifu(bun)
        navController.navigate(action)
    }*/

    /*fun onButtonFavoritesClicked(bun: Bundle) {
        val action = SelectorFragmentDirections.actionSelectorToFavorites(bun)
        navController.navigate(action)
    }*/

    /*fun onButtonGptClicked() {
        val action = SelectorFragmentDirections.actionSelectorToGpt()
        navController.navigate(action)
    }*/

    /*fun onButtonGeminiClicked() {
        val action = SelectorFragmentDirections.actionSelectorToGemini()
        navController.navigate(action)
    }*/

    fun requestPermissionLauncher(afterRequest: (Boolean) -> Unit) {
        scope.launch {
            val result = permissionRequester.request()
            afterRequest(result)
        }
    }

    fun errorToString(error: Error) = when (error) {
        Error.Connectivity -> context.getString(R.string.connectivity_error)
        is Error.Server -> context.getString(R.string.no_waifu_found) + error.code
        is Error.Unknown -> context.getString(R.string.unknown_error) + error.message
    }

}

/*fun Fragment.buildMainState(
    context: Context = requireContext(),
    scope: CoroutineScope = viewLifecycleOwner.lifecycleScope,
    navController: NavController = findNavController(),
    locationPermissionRequester: PermissionRequester = PermissionRequester(
        this,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
) = MainState(context, scope, navController, locationPermissionRequester)*/



