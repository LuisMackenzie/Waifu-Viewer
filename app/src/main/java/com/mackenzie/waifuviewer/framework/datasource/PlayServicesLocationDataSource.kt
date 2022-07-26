package com.mackenzie.waifuviewer.framework.datasource

import android.annotation.SuppressLint
import android.app.Application
import android.location.Location
import com.google.android.gms.location.LocationServices
import com.mackenzie.waifuviewer.data.datasource.LocationDataSource
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class PlayServicesLocationDataSource(application: Application) : LocationDataSource {
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)

    @SuppressLint("MissingPermission")
    override suspend fun findLastLocation(): Location? =
        suspendCancellableCoroutine { continuation ->
            fusedLocationClient.lastLocation
                .addOnCompleteListener {
                    continuation.resume(it.result)
                }
        }
}