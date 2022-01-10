package com.kosyakoff.distancetrackerapp.ui.bindingadapters

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter

object MapsBindingAdapter {

    @BindingAdapter("app:visibilityBasedOnTrackingStatus")
    @JvmStatic
    fun observeTracking(view: View, started: Boolean) {
        if (view is Button) {
            view.isVisible = started
        }
        if (view is TextView) {
            view.isVisible = !started
        }

    }
}