package com.kosyakoff.distancetrackerapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResultModel(var distance: String, var time: String) : Parcelable