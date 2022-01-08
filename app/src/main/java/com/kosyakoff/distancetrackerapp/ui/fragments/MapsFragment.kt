package com.kosyakoff.distancetrackerapp.ui.fragments

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.kosyakoff.distancetrackerapp.R
import com.kosyakoff.distancetrackerapp.databinding.FragmentMapsBinding

class MapsFragment : BaseFragment(R.layout.fragment_maps), OnMapReadyCallback {

    private val binding: FragmentMapsBinding by viewBinding(FragmentMapsBinding::bind)

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

    override fun onMapReady(p0: GoogleMap) {

    }
}