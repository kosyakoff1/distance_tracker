package com.kosyakoff.distancetrackerapp.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.kosyakoff.distancetrackerapp.R
import com.kosyakoff.distancetrackerapp.databinding.FragmentMapsBinding
import kotlinx.coroutines.delay

class MapsFragment : BaseFragment(R.layout.fragment_maps), OnMapReadyCallback,
    GoogleMap.OnMyLocationButtonClickListener {

    private val binding: FragmentMapsBinding by viewBinding(FragmentMapsBinding::bind)
    private lateinit var map: GoogleMap

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    override fun initViews() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        with(binding) {
            startButton.setOnClickListener {

            }
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        initMap()
    }

    @SuppressLint("MissingPermission")
    private fun initMap() {
        map.isMyLocationEnabled = true
        map.setOnMyLocationButtonClickListener(this)
        map.uiSettings.apply {
            isZoomControlsEnabled = false
            isZoomGesturesEnabled = false
            isRotateGesturesEnabled = false
            isTiltGesturesEnabled = false
            isCompassEnabled = false
            isScrollGesturesEnabled = false
        }
    }

    override fun onMyLocationButtonClick(): Boolean {
        with(binding) {
            hintTextView
                .animate()
                .alpha(0f)
                .withEndAction {
                    hintTextView.isVisible = false
                    lifecycleScope.launchWhenStarted {
                        delay(1000)
                        startButton.isVisible = true
                    }
                }
                .duration = 1500

        }
        return false
    }
}