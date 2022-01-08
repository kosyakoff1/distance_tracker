package com.kosyakoff.distancetrackerapp.util

import android.Manifest
import android.content.Context
import androidx.fragment.app.Fragment
import com.kosyakoff.distancetrackerapp.R
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
}