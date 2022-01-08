package com.kosyakoff.distancetrackerapp.navigation

import androidx.navigation.NavController
import com.kosyakoff.distancetrackerapp.ui.fragments.PermissionFragmentDirections

object Navigator {
    fun navigateToMaps(navController: NavController) = navController.navigate(
        PermissionFragmentDirections.actionPermissionFragmentToMapsFragment()
    )
}