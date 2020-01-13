package com.awkris.watchamovie

import android.app.Application
import androidx.core.app.NotificationManagerCompat
import com.awkris.watchamovie.di.apiModule
import com.awkris.watchamovie.di.dataStoreModule
import com.awkris.watchamovie.di.repositoryModule
import com.awkris.watchamovie.di.viewmodelModule
import com.awkris.watchamovie.utils.NotificationUtils
import com.facebook.stetho.Stetho
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class WatchAMovie : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@WatchAMovie)
            androidFileProperties()
            modules(listOf(apiModule, dataStoreModule, repositoryModule, viewmodelModule))
        }

        Timber.plant(Timber.DebugTree())
        if (BuildConfig.BUILD_TYPE.contentEquals("debug")) {
            Stetho.initializeWithDefaults(this)
        }

        NotificationUtils.createNotificationChannel(
            this,
            NotificationManagerCompat.IMPORTANCE_DEFAULT,
            getString(R.string.app_name),
            "Movie reminder channel"
        )
    }
}