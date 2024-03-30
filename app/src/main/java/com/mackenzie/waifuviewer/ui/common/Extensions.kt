package com.mackenzie.waifuviewer.ui.common

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.nfc.NfcManager
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.annotation.RequiresApi
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.mackenzie.waifuviewer.App
import com.mackenzie.waifuviewer.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = true): View =
    LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)

fun ImageView.loadUrl(url: String) {
    Glide.with(context)
        .load(url)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .transition(DrawableTransitionOptions.withCrossFade())
        .error(R.drawable.ic_error_grey)
        .into(this)
}

fun ImageView.loadUrlCenterCrop(url: String) {
    Glide.with(context)
        .load(url)
        .centerCrop()
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .transition(DrawableTransitionOptions.withCrossFade())
        .error(R.drawable.ic_error_grey)
        .into(this)
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

@RequiresApi(Build.VERSION_CODES.R)
fun Context.isNightModeActive(): Boolean {
    return resources.configuration.isNightModeActive
}

fun Context.isNavigationActive(): Boolean {
    val cm = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val adapter = cm.defaultDisplay
    return adapter != null
}

fun isNfcAvailable(context: Context): Boolean {
    val cm = context.getSystemService(Context.NFC_SERVICE) as NfcManager
    val adapter = cm.defaultAdapter
    return adapter != null
}

fun Context.isSystemNavBarVisible(): Boolean {
    val decorView = (this as? WindowManager)?.defaultDisplay?.run {
        val realDisplayMetrics = android.util.DisplayMetrics()
        Log.e("NavHostActivity", "realDisplayMetrics=$realDisplayMetrics")
        getRealMetrics(realDisplayMetrics)
        val realHeight = realDisplayMetrics.heightPixels
        val realWidth = realDisplayMetrics.widthPixels
        Log.e("NavHostActivity", "realHeight=$realHeight, realWidth=$realWidth")

        val displayMetrics = android.util.DisplayMetrics()
        Log.e("NavHostActivity", "displayMetrics=$displayMetrics")
        getMetrics(displayMetrics)
        val displayHeight = displayMetrics.heightPixels
        val displayWidth = displayMetrics.widthPixels
        Log.e("NavHostActivity", "displayHeight=$displayHeight, displayWidth=$displayWidth")

        realHeight - displayHeight > 0 || realWidth - displayWidth > 0
    }
    return decorView ?: false
}


fun Context.isSystemNavBarVisible2(): Boolean {
    val cm = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val realDisplay = cm.defaultDisplay
    val realMetrics = android.util.DisplayMetrics()
    Log.e("NavHostActivity", "realDisplay=$realDisplay, realMetrics=$realMetrics")
    return realDisplay != null && realMetrics.equals(realDisplay)
}

/*fun Context.isNavigationBarVisible(): Boolean {
    val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = windowManager.defaultDisplay
    val realSize = Point()
    val screenSize = Point()
    display.getRealSize(realSize)
    display.getSize(screenSize)
    return realSize.y != screenSize.y
}*/

/*inline fun <T> basicDiffUtil(
    crossinline areItemsTheSame: (T, T) -> Boolean = { old, new -> old == new },
    crossinline areContentsTheSame: (T, T) -> Boolean = { old, new -> old == new }
) = object : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean =
        areItemsTheSame(oldItem, newItem)

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean =
        areContentsTheSame(oldItem, newItem)
}*/

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

val Context.app: App get() = applicationContext as App
