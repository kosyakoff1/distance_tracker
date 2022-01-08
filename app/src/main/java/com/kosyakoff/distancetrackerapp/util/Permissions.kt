package com.kosyakoff.distancetrackerapp.util

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.fragment.app.Fragment
import com.kosyakoff.distancetrackerapp.R
import com.kosyakoff.distancetrackerapp.util.Constants.PERMISSION_BACKGROUND_LOCATION_REQUEST_CODE
import com.kosyakoff.distancetrackerapp.util.Constants.PERMISSION_LOCATION_REQUEST_CODE
import com.vmadalin.easypermissions.EasyPermissions

object Permissions {
    fun hasLocationPermission(context: Context): Boolean =
        EasyPermissions.hasPermissions(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        )

    fun requestLocationPermission(fragment: Fragment) = EasyPermissions.requestPermissions(
        fragment,
        fragment.getString(R.string.cannot_run_app_message),
        PERMISSION_LOCATION_REQUEST_CODE,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    fun hasBackgroundLocationPermission(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
        return true
    }

    fun requestBackgroundLocationPermission(fragment: Fragment) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            EasyPermissions.requestPermissions(
                fragment,
                fragment.getString(R.string.src_maps_background_location_needed_message),
                PERMISSION_BACKGROUND_LOCATION_REQUEST_CODE,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }
}