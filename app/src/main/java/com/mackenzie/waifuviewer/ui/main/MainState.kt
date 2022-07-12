package com.mackenzie.waifuviewer.ui.main

import android.Manifest
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.mackenzie.waifuviewer.models.Waifu
import com.mackenzie.waifuviewer.models.WaifuPic
import com.mackenzie.waifuviewer.ui.SelectorFragmentDirections
import com.mackenzie.waifuviewer.ui.common.PermissionRequester
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun Fragment.buildMainState(
    scope: CoroutineScope = viewLifecycleOwner.lifecycleScope,
    navController: NavController = findNavController(),
    locationPermissionRequester: PermissionRequester = PermissionRequester(
        this,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
) = MainState(scope, navController, locationPermissionRequester)

class MainState(
    private val scope: CoroutineScope,
    private val navController: NavController,
    private val permissionRequester: PermissionRequester
) {
    fun onWaifuClicked(waifu: Waifu) {
        val action = WaifuFragmentDirections.actionWaifuToDetail(waifu.url)
        navController.navigate(action)
    }

    fun onWaifuPicsClicked(url: String) {
        val action = WaifuFragmentDirections.actionWaifuDestToDetailDest(url)
        navController.navigate(action)
    }

    fun onButtonGetWaifuClicked(bun: Bundle) {
        val action = SelectorFragmentDirections.actionSelectorToWaifu(bun)
        navController.navigate(action)
    }

    fun requestPermissionLauncher(afterRequest: (Boolean) -> Unit) {
        scope.launch {
            val result = permissionRequester.request()
            afterRequest(result)
        }
    }

}