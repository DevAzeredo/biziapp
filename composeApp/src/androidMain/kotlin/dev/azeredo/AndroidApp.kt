package dev.azeredo

import android.app.Application
import dev.azeredo.di.initKoin

import org.koin.android.ext.koin.androidContext

class AndroidApp: Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@AndroidApp)
        }
    }
}