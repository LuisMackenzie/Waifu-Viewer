package com.mackenzie.waifuviewer.ui.common

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.nfc.NfcManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.annotation.LayoutRes
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getString
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DiffUtil
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.mackenzie.waifuviewer.App
import com.mackenzie.waifuviewer.BuildConfig
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.domain.DownloadModel
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.RemoteConfigValues
import com.mackenzie.waifuviewer.domain.ServerType
import com.mackenzie.waifuviewer.domain.ServerType.ENHANCED
import com.mackenzie.waifuviewer.domain.ServerType.NEKOS
import com.mackenzie.waifuviewer.domain.ServerType.NORMAL
import com.mackenzie.waifuviewer.domain.selector.SwitchState
import com.mackenzie.waifuviewer.ui.main.ui.MainTheme
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = true): View =
    LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)

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
        // minimumFetchIntervalInSeconds = Constants.RELEASEINTERVALINSECONDS
        minimumFetchIntervalInSeconds = Constants.DEBUGINTERVALINSECONDS
    }
    remoteConfig.setConfigSettingsAsync(configSettings)
    remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
        if (task.isSuccessful) {
            configValues = RemoteConfigValues(
                remoteConfig.getBoolean("nsfw_mode"),
                remoteConfig.getBoolean("waifu_gpt_service"),
                remoteConfig.getBoolean("waifu_gemini_service"),
                remoteConfig.getBoolean("automatic_server"),
                remoteConfig.getString("latest_server_version"),
                false,
                remoteConfig.getLong("server_mode").toInt(),
                getServerType(app),
            )
            apply {
                setNsfwMode(app, nsfwIsActive, gptIsActive, geminiIsActive)
                setAutoMode(AutoModeIsEnabled)
            }
        } else {
            Log.e("getRemoteConfig", "Hubo un Error al recuperar de remote config: ${task.exception}")
        }
    }
    return configValues
}

fun String.compareVersion(): Int {
    val currentVersion = BuildConfig.VERSION_NAME.removeVersionSuffix()
    return if (this.isNotEmpty()) {
        val currentVersionParts = currentVersion.split(".")
        val latestVersionParts = this.split(".")
        if (currentVersionParts.size == latestVersionParts.size) {
            for (i in currentVersionParts.indices) {
                if (currentVersionParts[i].toInt() < latestVersionParts[i].toInt()) {
                    return -1
                } else if (currentVersionParts[i].toInt() > latestVersionParts[i].toInt()) {
                    return 1
                }
            }
        }
        0
    } else {
        0
    }
}

suspend fun RemoteConfigValues.getLatestVersion(): RemoteConfigValues {
    val deferred = CompletableDeferred<RemoteConfigValues>()
    var configValues = this
    val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
    val configSettings = remoteConfigSettings {
        // minimumFetchIntervalInSeconds = Constants.RELEASEINTERVALINSECONDS
        minimumFetchIntervalInSeconds = Constants.DEBUGINTERVALINSECONDS
    }
    remoteConfig.setConfigSettingsAsync(configSettings)
    remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
        if (task.isSuccessful) {
            configValues.latestServerVersion = remoteConfig.getString("latest_server_version")
            deferred.complete(configValues)
        } else {
            Log.e("getRemoteConfig", "Hubo un Error al recuperar de remote config: ${task.exception}")
        }
    }
    return deferred.await()
}

fun String.removeVersionSuffix(): String {
    return this.substringBefore("-")
}

fun RemoteConfigValues.saveServerType(app: Activity) {
    val sharedPref = app.getPreferences(Context.MODE_PRIVATE)
    requireNotNull(sharedPref)
    with (sharedPref.edit()) {
        putString(Constants.SERVER_MODE, type?.value)
        putInt(Constants.LOADED_SELECTOR, mode)
        putBoolean(Constants.IS_FAVORITE_WAIFU, isFavorite)
        apply()
    }
    Log.v("SaveMode", "SERVER_MODE=${type}, isFavorite=${isFavorite}")
}

/*fun ServerType.saveServerType(app: Activity) {
    val sharedPref = app.getPreferences(Context.MODE_PRIVATE)
    requireNotNull(sharedPref)
    with (sharedPref.edit()) {
        putString(Constants.SERVER_MODE, this@saveServerType.value)
        // putBoolean(Constants.IS_FAVORITE_WAIFU, isFavorite)
        apply()
    }
    Log.v("SaveMode", "SERVER_MODE=${this.value}")
}*/

fun getServerType(app: Activity): ServerType {
    val sharedPref = app.getPreferences(Context.MODE_PRIVATE)
    val mode = sharedPref.getString(Constants.SERVER_MODE, NORMAL.value)
    Log.v("Get Type", "SERVER_MODE=${mode}")
    return when (mode) {
        NORMAL.value -> NORMAL
        ENHANCED.value -> ENHANCED
        NEKOS.value -> NEKOS
        else -> NORMAL
    }
}

fun getServerTypeByMode(mode: Int): ServerType {
    return when (mode) {
        0 -> NORMAL
        1 ->ENHANCED
        2 -> NEKOS
        else -> NORMAL
    }
}

fun getServerModeOnly(app: Activity): Int {
    val sharedPref = app.getPreferences(Context.MODE_PRIVATE)
    val mode = sharedPref.getInt(Constants.LOADED_SELECTOR, 0)
    Log.v("GetMode", "LOADED_SELECTOR=${mode}")
    return mode
}

/*fun saveBundle(ctx: Context, mode: ServerType?, switchValues: SwitchState, tag: String): Bundle {
    val bun = bundleOf()
    bun.putString(Constants.SERVER_MODE, mode?.value)
    bun.putBoolean(Constants.IS_NSFW_WAIFU, switchValues.nsfw)
    bun.putBoolean(Constants.IS_GIF_WAIFU, switchValues.gifs)
    bun.putBoolean(Constants.IS_LANDS_WAIFU, switchValues.portrait)
    bun.putString(Constants.CATEGORY_TAG_WAIFU, tagFilter(ctx, mode, switchValues, tag))
    mode?.saveServerType(ctx as Activity)
    Log.v("saveBundle", "SERVER_MODE=${mode?.value}")
    return bun
}*/


fun String.tagFilter(ctx: Context, mode: ServerType?, switchValues: SwitchState): String {
    var updatedTag: String = this
    if (this == getString(ctx, R.string.categories) || this == getString(ctx, R.string.categories_items)) {
        when (mode) {
            NORMAL, ENHANCED -> {
                updatedTag = getString(ctx, R.string.tag_waifu)
            }
            else -> {
                if (!switchValues.gifs) {
                    updatedTag = getString(ctx, R.string.tag_neko)
                } else {
                    updatedTag = getString(ctx, R.string.tag_pat)
                }
            }
        }
    }
    return updatedTag
}

private fun setAutoMode(isAutomatic: Boolean) {
    if (isAutomatic) {
        // TODO
        // Toast.makeText(requireContext(), "Automatic Mode Selected", Toast.LENGTH_SHORT).show()
    } else {
        // TODO
        // Toast.makeText(requireContext(), "Manual Mode Selected", Toast.LENGTH_SHORT).show()
    }
}

private fun setNsfwMode(app: Activity, nsfw: Boolean, hasGpt: Boolean, hasGemini: Boolean) {
    val sharedPref = app.getPreferences(Context.MODE_PRIVATE)
    requireNotNull(sharedPref)
    with (sharedPref.edit()) {
        putBoolean(Constants.WORK_MODE, nsfw)
        putBoolean(Constants.IS_WAIFU_GPT, hasGpt)
        putBoolean(Constants.IS_WAIFU_GEMINI, hasGemini)
        apply()
    }
}

fun String.showToast(context: Context) {
    Toast.makeText(context, this, Toast.LENGTH_LONG).show()
}

fun String.showShortToast(context: Context) {
    Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
}

fun Error.errorToString(ctx: Context) = when (this) {
    Error.Connectivity -> ctx.getString(R.string.connectivity_error)
    is Error.Server -> ctx.getString(R.string.no_waifu_found) + code
    is Error.Unknown -> ctx.getString(R.string.unknown_error) + message
}

fun Context.errorToString(error: Error) = when (error) {
    Error.Connectivity -> getString(R.string.connectivity_error)
    is Error.Server -> getString(R.string.no_waifu_found) + error.code
    is Error.Unknown -> getString(R.string.unknown_error) + error.message
}

fun getFirebaseInstance(): FirebaseAnalytics {
    return Firebase.analytics
}

/*fun Fragment.composeView(content: @Composable () -> Unit): ComposeView {
    return ComposeView(requireContext()).apply {
        setContent {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            MainTheme {
                content()
            }
        }
    }
}*/

/*fun onDownloadClick(download: DownloadModel, scope: CoroutineScope, context: Context, launcher: ManagedActivityResultLauncher<String, Boolean>) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        downloadImage(scope, context, download.title, download.link, download.imageExt)
    } else {
        if (!context.hasWriteExternalStoragePermission()) {
            scope.launch {
                launcher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        } else {
            downloadImage(scope, context, download.title, download.link, download.imageExt)
        }
    }
}*/

/*fun Context.hasLocationPermissionGranted(): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}*/

fun Context.hasWriteExternalStoragePermission(): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun Context.hasPushPermission(): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.POST_NOTIFICATIONS
    ) == PackageManager.PERMISSION_GRANTED
}

fun Activity.hasPushPermissionRationale(): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        if (this.shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
            return true
        }
    }
    return false
}

fun downloadImage(scope: CoroutineScope, ctx: Context, title: String, link: String, fileType: String) {
    val type: String = selectMimeType(fileType)
    scope.launch(IO) { SaveImage().saveImageToStorage(ctx, title, type, link) }
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

fun String.decodeMimeTypeForTitle(title: String): String {
    when (this) {
        "image/jpeg" -> return "$title.jpg"
        "image/jpg" -> return "$title.jpg"
        "image/png" -> return "$title.png"
        "image/gif" -> return "$title.gif"
        else -> return ".jpg"
    }
}

fun loadInitialServer(): ServerType {
    when (Build.VERSION.SDK_INT) {
        in 0..Build.VERSION_CODES.N -> { return ENHANCED } // Android 7 API 24 Hacia Abajo
        in 35..40 -> { return NEKOS } // Android 15 Hacia Arriba
        else -> { return NORMAL }
    }
}

@Composable
fun String.getSimpleText(): String {
    return when (this) {
        NORMAL.value -> stringResource(R.string.server_normal_toast)
        ENHANCED.value -> stringResource(R.string.server_enhanced_toast)
        NEKOS.value -> stringResource(R.string.server_best_toast)
        else -> stringResource(R.string.server_unknown_toast)
    }
}

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

fun Context.isLandscape(): Boolean {
    return resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
}

fun getCurrentLocalDateTime(): String {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return currentDateTime.format(formatter)
    } else {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return format.format(Date())
    }
}

fun getDefaultTokenValidity(): String {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val currentDateTime = LocalDateTime.now().plusDays(7)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return currentDateTime.format(formatter)
    } else {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        calendar.add(Calendar.DAY_OF_YEAR, 7)
        val newDate = calendar.time
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return format.format(newDate)
    }
}

fun Date.dateToString2(): String {
    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return format.format(this)
}

fun Date?.dateToString(): String {
    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val date = this
    date?.let {
        return format.format(it)
    } ?: run {
        return ""
    }
}

fun String.stringToDate(): Date? {
    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return format.parse(this)
}
