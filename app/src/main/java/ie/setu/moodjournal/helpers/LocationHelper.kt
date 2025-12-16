package ie.setu.moodjournal.helpers

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import androidx.activity.result.ActivityResultLauncher

// Location helper created with help of AI and Google API docs
class LocationHelper(
    private val activity: Activity,
    private val permissionLauncher: ActivityResultLauncher<Array<String>>
) {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var fallbackLatLng: LatLng = LatLng(53.3498, -6.2603) // Dublin fallback

    private var onLocationReady: ((LatLng) -> Unit)? = null

    fun init(onLocationReady: (LatLng) -> Unit) {
        this.onLocationReady = onLocationReady
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)

        checkPermissions()
    }

    private fun checkPermissions() {
        when {
            ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(
                        activity,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED -> {
                fetchLastLocation()
            }
            activity.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                showRationaleDialog()
            }
            else -> {
                // Request permissions first time
                permissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }
    }

    private fun showRationaleDialog() {
        AlertDialog.Builder(activity)
            .setTitle("Location Permission Needed")
            .setMessage(
                "We need access to your location so you can set your mood location " +
                        "on the map and easily return to your current location."
            )
            .setPositiveButton("Grant") { _, _ ->
                permissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
                onLocationReady?.invoke(fallbackLatLng)
            }
            .show()
    }

    @Suppress("MissingPermission")
    private fun fetchLastLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
            if (loc != null) {
                onLocationReady?.invoke(LatLng(loc.latitude, loc.longitude))
            } else {
                onLocationReady?.invoke(fallbackLatLng)
            }
        }
    }

    /**
     * Called by the activity when permissions result is returned
     */
    fun onPermissionResult(permissions: Map<String, Boolean>) {
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> fetchLastLocation()
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> fetchLastLocation()
            else -> onLocationReady?.invoke(fallbackLatLng)
        }
    }
}
