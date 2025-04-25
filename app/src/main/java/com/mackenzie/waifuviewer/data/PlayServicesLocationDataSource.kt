package com.mackenzie.waifuviewer.data

import android.annotation.SuppressLint
import android.app.Application
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.util.Log
import com.google.android.gms.location.LocationServices
import com.mackenzie.waifuviewer.data.PermissionChecker.Permission.COARSE_LOCATION
import com.mackenzie.waifuviewer.data.datasource.LocationDataSource
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import okio.IOException
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class PlayServicesLocationDataSource @Inject constructor(application: Application) : LocationDataSource {
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)
    private val geocoder = Geocoder(application)

    @SuppressLint("MissingPermission")
    override suspend fun findLastRegion(): String {
        val location : Location? = try {
            fusedLocationClient.lastLocation.await()
        } catch (e: Exception) {
            Log.e("PlayServicesLocationDataSource", "Error getting last location: ${e.message}")
            null
        }
        return location?.let { getRegionForLocation(it) } ?: "US"
    }

    /*@SuppressLint("MissingPermission")
    suspend fun findLastRegion2(): String? =
        suspendCancellableCoroutine { continuation ->
            fusedLocationClient.lastLocation
                .addOnCompleteListener {
                    continuation.resume(it.result.toRegion())
                }
        }*/

    private suspend fun getRegionForLocation(location: Location): String = suspendCancellableCoroutine { continuation ->
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            try {
                geocoder.getFromLocation(location.latitude, location.longitude, 1, object : Geocoder.GeocodeListener {
                    override fun onGeocode(addresses: MutableList<Address>) {
                        val countryCode = addresses.firstOrNull()?.countryCode
                        Log.d("PlayServicesLocationDataSource", "GeoCodeListener success: countryCode = $countryCode")
                        if (continuation.isActive) {
                            continuation.resume(countryCode ?: "US")
                        }
                    }

                    override fun onError(errorMessage: String?) {
                        super.onError(errorMessage)
                        Log.e("PlayServicesLocationDataSource", "GeoCodeListener Error: Error= $errorMessage")
                        if (continuation.isActive) {
                            continuation.resume("US")
                        }
                    }
                })

            } catch (e: Exception) {
                Log.e("PlayServicesLocationDataSource", "Error calling getFromLocation: ${e.message}")
                continuation.resumeWithException(IOException("Geocoder location error: ${e.message}"))
            }
        } else {
            try {
                @Suppress("DEPRECATION")
                val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                val countryCode = addresses?.firstOrNull()?.countryCode
                Log.d("PlayServicesLocationDataSource", "Legacy getFromLocation success: countryCode = $countryCode")
                if (continuation.isActive) {
                    continuation.resume(countryCode ?: "US")
                }
            } catch (e: IOException) {
                Log.e("PlayServicesLocationDataSource", "Legacy getFromLocation failed: ${e.message}")
                if (continuation.isActive) continuation.resume("US")
            } catch (e: Exception) {
                Log.e( "PlayServicesLocationDataSource", "Unexpected error in Legacy getFromLocation: ${e.message}")
                if (continuation.isActive) {
                    continuation.resumeWithException(e)
                }
            }
        }

        continuation.invokeOnCancellation {
            Log.d("PlayServicesLocationDataSource", "Geocoding Coroutine cancelled")
            // Handle cancellation if needed
        }
    }

    // TODO
    /*private fun Location?.toRegion2(): String? {
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
    }*/

    // TODO
    /*private fun Location?.toRegion3(): String? {
        val addresses = this?.let {
            geocoder.getFromLocation(latitude, longitude, 1)
        }
        Log.e("getFromLocation", "countryCode = ${addresses?.firstOrNull()?.countryCode}")
        return addresses?.firstOrNull()?.countryCode
    }*/
}