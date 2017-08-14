package com.companyname.moviecat

import android.app.Application

import com.moviecat.joe.BuildConfig

import timber.log.Timber

/**
 * Created by Joe on 3/25/2017.
 */

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
