package com.kosyakoff.distancetrackerapp.util

import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng

object MapUtils {
    fun setCameraPosition(location: LatLng): CameraPosition {
        return CameraPosition.builder()
            .target(location)
            .zoom(18f)
            .build()
    }
}