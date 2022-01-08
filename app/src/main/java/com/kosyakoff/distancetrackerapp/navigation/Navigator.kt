package com.kosyakoff.distancetrackerapp.navigation

import androidx.navigation.NavController
import com.kosyakoff.distancetrackerapp.PermissionFragmentDirections

object Navigator {
    fun navigateToMaps(navController: NavController) = navController.navigate(
        PermissionFragmentDirections.actionPermissionFragmentToMapsFragment()
    )
}