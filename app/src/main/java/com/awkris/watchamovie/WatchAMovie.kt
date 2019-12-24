package com.awkris.watchamovie

import android.app.Application
import com.awkris.watchamovie.di.component.ApplicationComponent
import com.awkris.watchamovie.di.component.DaggerApplicationComponent
import com.awkris.watchamovie.di.module.ApiModule
import com.awkris.watchamovie.di.module.ApplicationModule
import com.facebook.stetho.Stetho
import timber.log.Timber

class WatchAMovie : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        appComponent = createAppComponent()
        Timber.plant(Timber.DebugTree())
        if (BuildConfig.BUILD_TYPE.contentEquals("debug")) {
            Stetho.initializeWithDefaults(this)
        }
    }

    private fun createAppComponent(): ApplicationComponent {
        return DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(this))
            .apiModule(ApiModule())
            .build()
    }

    companion object {
        @JvmStatic
        lateinit var appComponent: ApplicationComponent
            private set
        lateinit var instance: WatchAMovie
            private set
    }
}