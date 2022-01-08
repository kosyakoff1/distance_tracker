package com.kosyakoff.distancetrackerapp

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kosyakoff.distancetrackerapp.databinding.FragmentPermissionBinding
import com.kosyakoff.distancetrackerapp.navigation.Navigator
import com.kosyakoff.distancetrackerapp.util.Permissions
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog

class PermissionFragment : Fragment(R.layout.fragment_permission),
    EasyPermissions.PermissionCallbacks {

    private val binding: FragmentPermissionBinding by viewBinding(FragmentPermissionBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            continueButton.setOnClickListener {
                if (Permissions.hasLocationPermission(requireContext())) {
                    Navigator.navigateToMaps(findNavController())
                } else {
                    Permissions.requestLocationPermission(this@PermissionFragment)
                }
            }
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            SettingsDialog.Builder(requireActivity()).build().show()
        } else {
            Permissions.requestLocationPermission(this)
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Navigator.navigateToMaps(findNavController())
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(
            requestCode, permissions, grantResults, this
        )
    }

}