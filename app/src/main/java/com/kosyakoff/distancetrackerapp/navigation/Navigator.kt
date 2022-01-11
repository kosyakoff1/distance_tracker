package com.kosyakoff.distancetrackerapp.navigation

import androidx.navigation.NavController
import com.kosyakoff.distancetrackerapp.model.ResultModel
import com.kosyakoff.distancetrackerapp.ui.fragments.MapsFragmentDirections
import com.kosyakoff.distancetrackerapp.ui.fragments.PermissionFragmentDirections

object Navigator {
    fun navigateToMaps(navController: NavController) = navController.navigate(
        PermissionFragmentDirections.actionPermissionFragmentToMapsFragment()
    )

    fun navigateToResults(navController: NavController, result: ResultModel) {
        val directions = MapsFragmentDirections.actionMapsFragmentToResultFragment(result)
        navController.navigate(directions)
    }
}