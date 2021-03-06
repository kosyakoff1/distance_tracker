package com.kosyakoff.distancetrackerapp.ui.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.kosyakoff.distancetrackerapp.R
import com.kosyakoff.distancetrackerapp.databinding.FragmentMapsBinding
import com.kosyakoff.distancetrackerapp.model.ResultModel
import com.kosyakoff.distancetrackerapp.navigation.Navigator
import com.kosyakoff.distancetrackerapp.service.TrackerService
import com.kosyakoff.distancetrackerapp.util.Constants
import com.kosyakoff.distancetrackerapp.util.MapUtils
import com.kosyakoff.distancetrackerapp.util.MapUtils.setCameraPosition
import com.kosyakoff.distancetrackerapp.util.Permissions
import com.kosyakoff.distancetrackerapp.util.getColorFromAttr
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MapsFragment : BaseFragment(R.layout.fragment_maps), OnMapReadyCallback,
    GoogleMap.OnMyLocationButtonClickListener, EasyPermissions.PermissionCallbacks, GoogleMap.OnMarkerClickListener {

    private val binding: FragmentMapsBinding by viewBinding(FragmentMapsBinding::bind)
    private lateinit var map: GoogleMap
    private var startTime = 0L
    private var stopTime = 0L
    private var polylineList = mutableListOf<Polyline>()
    private var markerList = mutableListOf<Marker>()
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    val started = MutableLiveData<Boolean>(false)
    private var locationList = mutableListOf<LatLng>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    override fun initViews() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        with(binding) {
            lifecycleOwner = viewLifecycleOwner

            binding.tracking = this@MapsFragment

            startButton.setOnClickListener {
                onStartButtonClicked()
            }
            stopButton.setOnClickListener {
                onStopButtonClicked()
            }
            resetButton.setOnClickListener {
                onResetButtonClicked()
            }
        }
    }

    private fun onResetButtonClicked() {
        mapReset()
    }

    @SuppressLint("MissingPermission")
    private fun mapReset() {
        fusedLocationProviderClient.lastLocation.addOnCompleteListener {
            val lastKnownLocaton = LatLng(it.result.latitude, it.result.longitude)

            map.animateCamera(
                CameraUpdateFactory.newCameraPosition(
                    setCameraPosition(lastKnownLocaton)
                )
            )
            for (polyline in polylineList) {
                polyline.remove()
            }

            for (marker in markerList) {
                marker.remove()
            }
            markerList.clear()
            locationList.clear()
            binding.resetButton.isVisible = false
            binding.startButton.isVisible = true
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        initMap()
        observeTrackerService()
        map.setOnMarkerClickListener(this)
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

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            SettingsDialog.Builder(requireContext()).build().show()
        } else {
            Permissions.requestBackgroundLocationPermission(this)
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        onStartButtonClicked()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        EasyPermissions.onRequestPermissionsResult(
            requestCode, permissions, grantResults, this
        )

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

    private fun observeTrackerService() {
        TrackerService.locationList.observe(viewLifecycleOwner) {
            if (it != null) {
                locationList = it

                if (locationList.size > 1) {
                    binding.stopButton.isEnabled = true
                }

                drawPolyline()
                followPolyline()
            }
        }
        TrackerService.started.observe(viewLifecycleOwner) {
            started.value = it
        }
        TrackerService.startTime.observe(viewLifecycleOwner) { startTime = it }
        TrackerService.stopTime.observe(viewLifecycleOwner) {
            stopTime = it
            if (stopTime != 0L) {
                showBiggerPicture()
                displayResult()
            }

        }
    }

    private fun displayResult() {

        lifecycleScope.launch {
            delay(2500)
            val result = ResultModel(
                MapUtils.calculateDistance(locationList),
                MapUtils.calculateElapsedTime(startTime, stopTime)
            )
            Navigator.navigateToResults(findNavController(), result)
            binding.startButton.apply {
                isVisible = false
                isEnabled = true
            }
            binding.stopButton.apply {
                isVisible = false
            }
            binding.resetButton.isVisible = true
        }
    }

    private fun showBiggerPicture() {
        val bounds = LatLngBounds.Builder()
        for (location in locationList) {
            bounds.include(location)
        }
        map.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds.build(), 100,
            ), 2000, null
        )
        addMarker(locationList.first())
        addMarker(locationList.last())
    }

    private fun addMarker(position: LatLng) {
        val marker = map.addMarker(MarkerOptions().position(position))
        markerList.add(marker!!)
    }

    private fun drawPolyline() {
        var polyline = map.addPolyline(
            PolylineOptions().apply {
                width(10f)
                color(Color.BLUE)
                jointType(JointType.ROUND)
                startCap(ButtCap())
                endCap(ButtCap())
                addAll(locationList)
            }
        )
        polylineList.add(polyline)
    }

    private fun followPolyline() {
        if (locationList.isNotEmpty()) {
            map.animateCamera(
                CameraUpdateFactory.newCameraPosition(
                    MapUtils.setCameraPosition(locationList.last())
                ), 1000, null
            )
        }
    }

    private fun onStartButtonClicked() {
        if (!Permissions.hasBackgroundLocationPermission(requireContext())) {
            Permissions.requestBackgroundLocationPermission(this)
            return
        }
        with(binding) {
            startButton.isVisible = false
            stopButton.isVisible = true
        }
        startCountDown()
    }

    private fun onStopButtonClicked() {
        stopForegroundService()
        binding.stopButton.isVisible = false
    }

    private fun stopForegroundService() {
        binding.startButton.isEnabled = false
        sendActionCommandToService(Constants.ACTION_SERVICE_STOP)
    }

    private fun sendActionCommandToService(action: String) {
        Intent(requireContext(), TrackerService::class.java).apply {
            this.action = action
            requireContext().startService(this)
        }
    }

    private fun startCountDown() {
        with(binding) {
            timerTextView.isVisible = true
            stopButton.isEnabled = false
            val timer: CountDownTimer = object : CountDownTimer(4000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val currentSecond = millisUntilFinished / 1000

                    if (currentSecond > 0) {
                        timerTextView.text = currentSecond.toString()
                        //timerTextView.setTextColor(requireContext().getThemeColor(R.attr.colorWarning))
                    } else {
                        timerTextView.text = getString(R.string.scr_map_go_message)
                        timerTextView.setTextColor(requireContext().getColorFromAttr(R.attr.colorCountDownGo))
                    }
                }

                override fun onFinish() {
                    sendActionCommandToService(Constants.ACTION_SERVICE_START)
                    timerTextView.isVisible = false
                }
            }

            timer.start()
        }
    }

    override fun onMarkerClick(p0: Marker): Boolean = true

}