package com.example.story_app.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import com.example.story_app.R
import com.example.story_app.data.local.AuthPreference
import com.example.story_app.data.response.ListStoryItem

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.story_app.databinding.ActivityMapsBinding
import com.example.story_app.ui.story.StoryViewModel
import com.example.story_app.ui.story.ViewmodelFactory
import com.google.android.gms.maps.model.LatLngBounds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private var boundsBuilder = LatLngBounds.Builder()
    private val indonesianBounds = ArrayList<ListStoryItem>()
    private val viewModel: StoryViewModel by viewModels {
        ViewmodelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val pref = AuthPreference(this)
        val token = pref.getUser().token
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
        viewModel.listStoryMap.observe(this) { list ->
            list.map { item ->
                if (item.lat!! > -11.00 && item.lat < 6.00 && item.lon!! > 95.00 && item.lon < 141.00) {
                    indonesianBounds.add(item)
                }
            }

            if (indonesianBounds.isNotEmpty()) {
                indonesianBounds.map { indonesianLocated ->
                    indonesianLocated.lat?.let { indonesianLocated.lon?.let { it1 -> LatLng(it, it1) } }?.let {
                        MarkerOptions()
                            .position(it)
                            .snippet(indonesianLocated.description)
                            .title(indonesianLocated.name)
                    }?.let {
                        mMap.addMarker(
                            it
                        )
                    }

                    indonesianLocated.lon?.let {
                        indonesianLocated.lat?.let { it1 ->
                            LatLng(
                                it1,
                                it
                            )
                        }
                    }?.let { boundsBuilder.include(it) }
                }

                val bounds: LatLngBounds = boundsBuilder.build()
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 64))
            }
        }
        GlobalScope.launch(Dispatchers.Main) {
            // Assuming you have a token variable to pass


            // Call the suspend function
            viewModel.getListStoryMap(token)
        }

    }
}