package com.mackenzie.waifuviewer.ui.common

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.nfc.NfcManager
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.LayoutRes
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DiffUtil
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.mackenzie.waifuviewer.App
import com.mackenzie.waifuviewer.domain.DownloadModel
import com.mackenzie.waifuviewer.domain.RemoteConfigValues
import com.mackenzie.waifuviewer.domain.ServerType
import com.mackenzie.waifuviewer.domain.ServerType.ENHANCED
import com.mackenzie.waifuviewer.domain.ServerType.NEKOS
import com.mackenzie.waifuviewer.domain.ServerType.NORMAL
import com.mackenzie.waifuviewer.ui.common.PermissionRequesterTest.Companion.register
import com.mackenzie.waifuviewer.ui.main.buildMainState
import com.mackenzie.waifuviewer.ui.main.ui.MainTheme
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.URL

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = true): View =
    LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)

/*fun ImageView.loadUrl(url: String) {
    Glide.with(context)
        .load(url)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .transition(DrawableTransitionOptions.withCrossFade())
        .error(R.drawable.ic_error_grey)
        .into(this)
}*/

// Unnussed
/*fun ImageView.loadUrlCenterCrop(url: String) {
    Glide.with(context)
        .load(url)
        .centerCrop()
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .transition(DrawableTransitionOptions.withCrossFade())
        .error(R.drawable.ic_error_grey)
        .into(this)
}*/

fun RemoteConfigValues.getConfig(app: Activity): RemoteConfigValues {
    val temp = CompletableDeferred<RemoteConfigValues>()
    var configValues = this
    val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
    val configSettings = remoteConfigSettings {
        minimumFetchIntervalInSeconds = Constants.RELEASEINTERVALINSECONDS
        // minimumFetchIntervalInSeconds = Constants.DEBUGINTERVALINSECONDS
    }
    remoteConfig.setConfigSettingsAsync(configSettings)
    remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
        if (task.isSuccessful) {
            configValues = RemoteConfigValues(
                remoteConfig.getBoolean("nsfw_mode"),
                remoteConfig.getBoolean("waifu_gpt_service"),
                remoteConfig.getBoolean("waifu_gemini_service"),
                remoteConfig.getBoolean("automatic_server"),
                false,
                remoteConfig.getLong("server_mode").toInt(),
                getServerMode(app),
            )
            apply {
                // setNsfwMode(nsfwIsActive, gptIsActive, geminiIsActive)
                // setAutoMode(AutoModeIsEnabled)
            }
        } else {
            Log.e("getRemoteConfig", "Hubo un Error al recuperar de remote config: ${task.exception}")
        }
    }
    return configValues
}

fun getServerMode(app: Activity): ServerType {
    val sharedPref = app.getPreferences(Context.MODE_PRIVATE)
    val mode = sharedPref.getString(Constants.SERVER_MODE, NORMAL.value)
    Log.v("GetMode", "SERVER_MODE=${mode}")
    return when (mode) {
        NORMAL.value -> NORMAL
        ENHANCED.value -> ENHANCED
        NEKOS.value -> NEKOS
        else -> NORMAL
    }
}

fun String.showToast(context: Context) {
    Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
}

fun Activity.showFullscreenCutout() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
    }
}

fun Activity.showBelowCutout() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER
    }
}

fun Fragment.composeView(content: @Composable () -> Unit): ComposeView {
    return ComposeView(requireContext()).apply {
        setContent {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            MainTheme {
                content()
            }
        }
    }

}

fun onDownloadClick(isGranted:Boolean, download: DownloadModel, scope: CoroutineScope, context: Context) {
    if (!isGranted) {
        RequestPermision(scope)
        // RequestPermision(context, scope)
    } else {
        downloadImage(scope, context, download.title, download.link, download.imageExt)
    }
}

private fun RequestPermision(scope: CoroutineScope) {
    scope.launch {

    }
}

private fun RequestPermision2(ctx: Context, scope: CoroutineScope) {

    when {
        ContextCompat.checkSelfPermission(
            ctx,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED -> {
            // You can use the API that requires the permission.
        }
        ActivityCompat.shouldShowRequestPermissionRationale(
            this, Manifest.permission.READ_EXTERNAL_STORAGE) -> {
            // In an educational UI, explain to the user why your app requires this
            // permission for a specific feature to behave as expected, and what
            // features are disabled if it's declined. In this UI, include a
            // "cancel" or "no thanks" button that lets the user continue
            // using your app without granting the permission.

            // showInContextUI(...)
        }
        else -> {
            // You can directly ask for the permission.
            // The registered ActivityResultCallback gets the result of this request.

            // requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    scope.launch {

    }
}

private fun downloadImage(scope: CoroutineScope, ctx: Context, title: String, link: String, fileType: String) {
    val type: String = selectMimeType(fileType)

    scope.launch(IO) {
        Log.e("DownloadImage", "Image downloading.....")
        SaveImage().saveImageToStorage(
            ctx,
            // image,
            title,
            type,
            link
        )
        /*try {
            // val url = URL(link)
            // val image: Bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream())
            SaveImage().saveImageToStorage(
                ctx,
                // image,
                title,
                type,
                link
            )
        } catch (e: IOException) {
            Log.d(Constants.CATEGORY_TAG_DETAIL, "error: ${e.localizedMessage}")
        }*/
    }
}

private fun selectMimeType(fileType: String): String {
    when (fileType) {
        "jpg" -> return "image/jpeg"
        "jpeg" -> return "image/jpeg"
        "png" -> return "image/png"
        "gif" -> return "image/gif"
        else -> {
            return "image/jpeg"
        }
    }
}



@RequiresApi(Build.VERSION_CODES.R)
fun Context.isNightModeActive(): Boolean {
    return resources.configuration.isNightModeActive
}
// Deprecated
/*fun Context.isNavigationActive2(): Boolean {
    val cm = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val adapter = cm.defaultDisplay
    return adapter != null
}*/

// Deprecated
/*fun Context.isNavigationActive(): Boolean {
    val displayManager = getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
    val displays = displayManager.getDisplays()
    displays.forEach {
        Log.e("NavHostActivity", "isNotEmpty=${displays.isNotEmpty()}, flags=${it.flags} height=${it.height}, width=${it.width}")
        Log.e("NavHostActivity", "isHdr=${it.isHdr} isValid=${it.isValid}, displayId=${it.displayId}")
    }
    return displays.isNotEmpty()
}*/

fun isNfcAvailable(context: Context): Boolean {
    val cm = context.getSystemService(Context.NFC_SERVICE) as NfcManager
    val adapter = cm.defaultAdapter
    return adapter != null
}

// Deprecated
/*fun Context.isSystemNavBarVisible(): Boolean {
    val decorView = (this as? WindowManager)?.defaultDisplay?.run {
        val realDisplayMetrics = android.util.DisplayMetrics()
        getRealMetrics(realDisplayMetrics)
        val realHeight = realDisplayMetrics.heightPixels
        val realWidth = realDisplayMetrics.widthPixels
        Log.e("NavHostActivity", "realHeight=$realHeight, realWidth=$realWidth")

        val displayMetrics = android.util.DisplayMetrics()
        getMetrics(displayMetrics)
        val displayHeight = displayMetrics.heightPixels
        val displayWidth = displayMetrics.widthPixels
        Log.e("NavHostActivity", "displayHeight=$displayHeight, displayWidth=$displayWidth")

        realHeight - displayHeight > 0 || realWidth - displayWidth > 0
    }
    Log.e("NavHostActivity", "decorView=$decorView")
    return decorView ?: false
}*/

// Deprecated
/*fun Context.isSystemNavBarVisible2(): Boolean {
    val cm = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val realDisplay = cm.defaultDisplay
    val realMetrics = android.util.DisplayMetrics()
    Log.e("NavHostActivity", "realDisplay=$realDisplay, realMetrics=$realMetrics")
    return realDisplay != null && realMetrics.equals(realDisplay)
}*/

// Deprecated
/*fun Context.isNavigationBarVisible(): Boolean {
    val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = windowManager.defaultDisplay
    val realSize = Point()
    val screenSize = Point()
    display.getRealSize(realSize)
    display.getSize(screenSize)
    return realSize.y != screenSize.y
}*/

inline fun <T : Any> basicDiffUtil(
    crossinline areItemsTheSame: (T, T) -> Boolean = { old, new -> old == new },
    crossinline areContentsTheSame: (T, T) -> Boolean = { old, new -> old == new }
) = object : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean =
        areItemsTheSame(oldItem, newItem)

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean =
        areContentsTheSame(oldItem, newItem)
}

var View.visible: Boolean
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }

fun <T> LifecycleOwner.launchAndCollect(
    flow: Flow<T>, state:
    Lifecycle.State = Lifecycle.State.STARTED,
    body: (T) -> Unit) {
    lifecycleScope.launch {
        this@launchAndCollect.repeatOnLifecycle(state) {
            flow.collect(body)
        }
    }
}

@Composable
fun <T> ComposableWrapper(value: T, body: @Composable (T) -> Unit) {
    body(value)
}

val Context.app: App get() = applicationContext as App

fun Context.isPortrait(): Boolean {
    return resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
}

fun Context.isLandscape(): Boolean {
    return resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
}
