package com.dicoding.picodiploma.loginwithanimation.maps

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityMapsBinding
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.maps.MapsViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val mapsViewModel by viewModels<MapsViewModel> {
        ViewModelFactory.getInstance(this)
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
        mMap = googleMap

        // Dapatkan token dari sesi pengguna
        mapsViewModel.getSession().observe(this, Observer { user ->
            val token = user.token
            if (token.isNotEmpty()) {
                mapsViewModel.getStoriesWithLocation(token).observe(this, Observer { stories ->
                    stories?.forEach { story ->
                        val lat = story?.lat ?: 0.0
                        val lon = story?.lon ?: 0.0
                        val latLng = LatLng(lat, lon)
                        if (story != null) {
                            mMap.addMarker(
                                MarkerOptions()
                                    .position(latLng)
                                    .title(story.name)
                                    .snippet(story.description)
                            )
                        }
                    }
                    if (stories != null) {
                        if (stories.isNotEmpty()) {
                            val firstStory = stories[0]
                            val firstLocation = LatLng(firstStory?.lat ?: 0.0, firstStory?.lon ?: 0.0)
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLocation, 10f))
                        }
                    }
                })
            }
        })
    }
}
