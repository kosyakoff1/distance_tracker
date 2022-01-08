package com.kosyakoff.distancetrackerapp.service

import android.content.Intent
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.kosyakoff.distancetrackerapp.util.Constants

class TrackerService : LifecycleService() {

    companion object {
        val started = MutableLiveData<Boolean>()

        private fun setInitialValues() {
            started.postValue(false)
        }
    }

    override fun onCreate() {
        setInitialValues()
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                Constants.ACTION_SERVICE_START -> {
                    started.postValue(true)
                }
                Constants.ACTION_SERVICE_STOP -> {
                    started.postValue(false)
                }
                else -> {

                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }
}