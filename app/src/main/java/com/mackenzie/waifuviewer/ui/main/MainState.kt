package com.mackenzie.waifuviewer.ui.main

import android.Manifest
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.data.Error
import com.mackenzie.waifuviewer.data.db.WaifuImItem
import com.mackenzie.waifuviewer.data.db.WaifuPicItem
import com.mackenzie.waifuviewer.ui.SelectorFragmentDirections
import com.mackenzie.waifuviewer.ui.common.PermissionRequester
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainState(
    private val context: Context,
    private val scope: CoroutineScope,
    private val navController: NavController,
    private val permissionRequester: PermissionRequester
) {
    fun onWaifuClicked(waifu: WaifuImItem) {
        val action = WaifuFragmentDirections.actionWaifuImToDetail(waifu.id)
        navController.navigate(action)
    }

    fun onWaifuPicsClicked(waifu: WaifuPicItem) {
        val action = WaifuFragmentDirections.actionWaifuPicsToDetail(waifu.id)
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

    fun errorToString(error: Error) = when (error) {
        Error.Connectivity -> context.getString(R.string.connectivity_error)
        is Error.Server -> context.getString(R.string.no_waifu_found) + error.code
        is Error.Unknown -> context.getString(R.string.unknown_error) + error.message
    }

}

fun Fragment.buildMainState(
    context: Context = requireContext(),
    scope: CoroutineScope = viewLifecycleOwner.lifecycleScope,
    navController: NavController = findNavController(),
    locationPermissionRequester: PermissionRequester = PermissionRequester(
        this,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
) = MainState(context, scope, navController, locationPermissionRequester)