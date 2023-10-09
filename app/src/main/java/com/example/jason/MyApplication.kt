package com.example.jason

import android.app.Activity
import android.app.Application
import android.os.Bundle
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication: Application(), Application.ActivityLifecycleCallbacks {

    var isAppIsRunning = false

    override fun onCreate() {
        super.onCreate()

        registerActivityLifecycleCallbacks(this)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        isAppIsRunning = true
    }

    override fun onActivityStarted(activity: Activity) {
        isAppIsRunning = true
    }

    override fun onActivityResumed(activity: Activity) {
        isAppIsRunning = true
    }

    override fun onActivityPaused(activity: Activity) = Unit

    override fun onActivityStopped(activity: Activity) {
        isAppIsRunning = false
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit

    override fun onActivityDestroyed(activity: Activity) {
        isAppIsRunning = false
    }
}