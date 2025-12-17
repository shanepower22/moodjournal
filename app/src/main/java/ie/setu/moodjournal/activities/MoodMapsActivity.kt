package ie.setu.moodjournal.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import ie.setu.moodjournal.databinding.ActivityMoodMapBinding
import ie.setu.moodjournal.databinding.ContentMoodMapBinding
import ie.setu.moodjournal.main.MainApp
import androidx.core.graphics.createBitmap
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.material.navigation.NavigationView
import com.squareup.picasso.Picasso
import ie.setu.moodjournal.R
import ie.setu.moodjournal.helpers.LocationHelper

class MoodMapsActivity : AppCompatActivity(), GoogleMap.OnMarkerClickListener {

    private lateinit var binding: ActivityMoodMapBinding
    private lateinit var contentBinding: ContentMoodMapBinding
    lateinit var app: MainApp
    lateinit var map: GoogleMap
    private lateinit var locationHelper: LocationHelper
    private lateinit var locationPermissionLauncher: ActivityResultLauncher<Array<String>>

    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoodMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        app = application as MainApp

        //setup user location
        locationPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            locationHelper.onPermissionResult(permissions)
        }
        locationHelper = LocationHelper(this, locationPermissionLauncher)

        contentBinding = binding.include
        contentBinding.mapView.onCreate(savedInstanceState)
        contentBinding.mapView.getMapAsync {
            map = it
            configureMap()
        }
        drawerLayout = binding.drawerLayout
        navView = binding.navView
        // Setup DrawerToggle
        drawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            binding.toolbar,
            R.string.nav_drawer_open,
            R.string.nav_drawer_close
        )
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        // Handle nav item clicks
        navView.setNavigationItemSelectedListener { menuItem ->
            when(menuItem.itemId) {
                R.id.nav_mood_list -> {
                    val intent = Intent(this, MoodListActivity::class.java)
                    startActivity(intent)
                    drawerLayout.closeDrawers()
                }
                R.id.nav_map -> drawerLayout.closeDrawers() // already here
            }
            true
        }


    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val tag = marker.tag as Long
        app.moodEntries.findById(tag) { mood ->

        contentBinding.currentTitle.text = mood!!.moodLabel
        contentBinding.currentDescription.text = mood.notes
        contentBinding.currentDate.text = mood.date.toString()
        Picasso.get().load(mood.imageUri).into(contentBinding.moodImage)
        }

        return false
    }

    private fun configureMap() {
        map.uiSettings.isZoomControlsEnabled = true
        app.moodEntries.findAll().forEach {
            val loc = LatLng(it.lat, it.lng)
            val options = MarkerOptions()
                .title(it.moodLabel)
                .position(loc)
                .icon(
                    getTintedMarker(
                        R.drawable.baseline_location_on_24,
                        it.moodColor
                    )
                )
            map.addMarker(options)?.tag = it.id
            map.setOnMarkerClickListener(this)

        }
        locationHelper.init() { userLocation ->
            if (locationHelper.hasLocationPermission()) {
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15f))
                enableMyLocationLayer()
            }
        }
    }

    //enables blue location dot and reset to my location button
    @SuppressLint("MissingPermission")
    private fun enableMyLocationLayer() {
        map.isMyLocationEnabled = true
        map.uiSettings.isMyLocationButtonEnabled = true
    }

    override fun onDestroy() {
        super.onDestroy()
        contentBinding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        contentBinding.mapView.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        contentBinding.mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        contentBinding.mapView.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        contentBinding.mapView.onSaveInstanceState(outState)
    }

    // AI generated helper function to get tinted marker
    private fun getTintedMarker(
        @DrawableRes drawableId: Int,
        color: Int

    ): BitmapDescriptor {

        val drawable = ContextCompat.getDrawable(this, drawableId)!!
        DrawableCompat.setTint(drawable, color)

        val bitmap = createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight)

        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}