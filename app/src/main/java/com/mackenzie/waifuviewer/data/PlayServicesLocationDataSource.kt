package com.mackenzie.waifuviewer.data

import android.annotation.SuppressLint
import android.app.Application
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.util.Log
import com.google.android.gms.location.LocationServices
import com.mackenzie.waifuviewer.data.datasource.LocationDataSource
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class PlayServicesLocationDataSource @Inject constructor(application: Application) : LocationDataSource {
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)
    private val geocoder = Geocoder(application)

    @SuppressLint("MissingPermission")
    override suspend fun findLastRegion(): String? =
        suspendCancellableCoroutine { continuation ->
            fusedLocationClient.lastLocation
                .addOnCompleteListener {
                    continuation.resume(it.result.toRegion())
                }
        }

    // TODO
    private fun Location?.toRegion(): String? {
        var newCC: String? = null
        this?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                geocoder.getFromLocation(latitude, longitude, 1, object : Geocoder.GeocodeListener{
                    override fun onGeocode(addresses: MutableList<Address>) {
                        newCC = addresses.firstOrNull()?.countryCode
                        Log.e("getFromLocation", "countryCode = $newCC")
                    }
                    override fun onError(errorMessage: String?) {
                        super.onError(errorMessage)
                        Log.e("getFromLocation", "Error= $errorMessage")
                    }
                })
            } else {
                newCC = geocoder.getFromLocation(latitude, longitude, 1)?.firstOrNull()?.countryCode
            }
        }
        return newCC ?: "US"
    }

    // TODO
    private fun Location?.toRegion2(): String? {
        val addresses = this?.let {
            geocoder.getFromLocation(latitude, longitude, 1)
        }
        Log.e("getFromLocation", "countryCode = ${addresses?.firstOrNull()?.countryCode}")
        return addresses?.firstOrNull()?.countryCode
    }
}