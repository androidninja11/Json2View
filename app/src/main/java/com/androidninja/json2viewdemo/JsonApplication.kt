package com.androidninja.json2viewdemo

import android.app.Application
import timber.log.Timber

class JsonApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}