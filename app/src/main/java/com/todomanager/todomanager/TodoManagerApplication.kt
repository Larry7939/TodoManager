package com.todomanager.todomanager

import android.app.Application
import com.todomanager.todomanager.BuildConfig
import timber.log.Timber

class TodoManagerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            initTimber()
        }
    }

    private fun initTimber() {
        Timber.plant(Timber.DebugTree())
    }
}