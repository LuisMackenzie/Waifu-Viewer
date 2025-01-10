package com.mackenzie.waifuviewer.ui.common.ui

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.domain.DownloadModel
import com.mackenzie.waifuviewer.ui.common.downloadImage
import com.mackenzie.waifuviewer.ui.common.hasWriteExternalStoragePermission
import com.mackenzie.waifuviewer.ui.common.showShortToast

@Composable
fun PermissionRequestEffect(permission: String, onResult: (Boolean) -> Unit) {
    val permissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {
            onResult(it)
        }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(permission)
    }
}

@Composable
fun MultiplePermissionRequestEffect(permissions: List<String>, onResult: (Map<String, Boolean>) -> Unit) {

    val multiplePermissionsLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
            // 'result' es un Map<String, Boolean>, donde la key es el permiso
            // y el value indica si fue concedido (true) o denegado (false).
            onResult(result)

    }

    LaunchedEffect(Unit) {
        multiplePermissionsLauncher.launch(permissions.toTypedArray())
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun PermissionPushRequestEffect(onResult: (Boolean) -> Unit) {
    val permissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {
            onResult(it)
        }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
    }
}

@Composable
fun PermissionDownloadRequestEffect(download: DownloadModel, onResult: (Boolean) -> Unit) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val permissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {
            onResult(it)
        }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        stringResource(R.string.waifus_detail_downloading).showShortToast(context)
        downloadImage(coroutineScope, context, download.title, download.link, download.imageExt)
    } else {
        if (!context.hasWriteExternalStoragePermission()) {
            LaunchedEffect(Unit) {
                permissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        } else {
            stringResource(R.string.waifus_detail_downloading).showShortToast(context)
            downloadImage(coroutineScope, context, download.title, download.link, download.imageExt)
        }
    }
}