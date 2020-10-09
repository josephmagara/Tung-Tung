package com.example.tungtung.application

import android.app.Activity
import android.app.Application
import android.os.Bundle
import dagger.hilt.android.HiltAndroidApp

/**
 * Created by josephmagara on 9/10/20.
 */

@HiltAndroidApp
class TungTungApplication : Application() {

    private var currentActivity: Activity? = null

    override fun onCreate() {
        super.onCreate()
        setupActivityListener()
    }

    fun getActiveActivity(): Activity? {
        return currentActivity
    }

    private fun setupActivityListener() {
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, p1: Bundle?) {
                currentActivity = activity;
            }

            override fun onActivityStarted(activity: Activity) {
                currentActivity = activity;
            }

            override fun onActivityResumed(activity: Activity) {
                currentActivity = activity;
            }

            override fun onActivityPaused(activity: Activity) {
                currentActivity = null;
            }

            override fun onActivityStopped(activity: Activity) {
            }

            override fun onActivitySaveInstanceState(activity: Activity, p1: Bundle) {
            }

            override fun onActivityDestroyed(activity: Activity) {
            }

        })
    }
}