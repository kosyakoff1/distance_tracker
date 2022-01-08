package com.kosyakoff.distancetrackerapp.ui.fragments

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

abstract class BaseFragment(@LayoutRes resId: Int) : Fragment(resId) {
    protected abstract fun initViews()
}