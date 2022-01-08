package com.kosyakoff.distancetrackerapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.kosyakoff.distancetrackerapp.R
import com.kosyakoff.distancetrackerapp.navigation.Navigator
import com.kosyakoff.distancetrackerapp.util.Permissions

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        navController = navHostFragment.navController

        if (Permissions.hasLocationPermission(this)) {
            Navigator.navigateToMaps(navController)
        }
    }
}