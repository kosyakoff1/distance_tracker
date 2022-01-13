package com.kosyakoff.distancetrackerapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kosyakoff.distancetrackerapp.R
import com.kosyakoff.distancetrackerapp.databinding.FragmentPermissionBinding
import com.kosyakoff.distancetrackerapp.navigation.Navigator
import com.kosyakoff.distancetrackerapp.util.Permissions
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog

class PermissionFragment : BaseFragment(R.layout.fragment_permission),
    EasyPermissions.PermissionCallbacks {

    private val binding: FragmentPermissionBinding by viewBinding(FragmentPermissionBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
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

    override fun initViews() {
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        EasyPermissions.onRequestPermissionsResult(
            requestCode, permissions, grantResults, this
        )
    }

}