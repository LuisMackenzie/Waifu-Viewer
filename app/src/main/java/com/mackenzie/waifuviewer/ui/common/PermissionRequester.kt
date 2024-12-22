package com.mackenzie.waifuviewer.ui.common

import android.app.Activity
import android.content.Context
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class PermissionRequester(fragment: Fragment, private val permission: String) {

    private var onRequest: (Boolean) -> Unit = {}
    private val launcher =
        fragment.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            onRequest(isGranted)
        }

    suspend fun request(): Boolean =
        suspendCancellableCoroutine { continuation ->
            onRequest = {
                continuation.resume(it)
            }
            launcher.launch(permission)
        }
}


class PermissionRequesterTest(
    // private val context: Context,
    private val permission: String,
    private val launcher: ActivityResultLauncher<String>
) {

    private var onRequest: (Boolean) -> Unit = {}

    suspend fun request(): Boolean =
        suspendCancellableCoroutine { continuation ->
            onRequest = {
                continuation.resume(it)
            }
            launcher.launch(permission)
        }

    companion object {
        fun register(
            lifecycleOwner: LifecycleOwner,
            onRequest: (Boolean) -> Unit
        ): ActivityResultLauncher<String> {
            return (lifecycleOwner as Activity).registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted ->
                onRequest(isGranted)
            }
        }
    }
}