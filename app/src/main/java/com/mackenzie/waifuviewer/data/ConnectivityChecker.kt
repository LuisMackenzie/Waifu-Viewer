package com.mackenzie.waifuviewer.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.nfc.NfcManager

fun checkDeviceConnectivity(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
    return when {
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_USB) -> true
        else -> false
    }
}

fun isNfcAvailable(context: Context): Boolean {
    val cm = context.getSystemService(Context.NFC_SERVICE) as NfcManager
    val adapter = cm.defaultAdapter
    return adapter != null
}