package com.awkris.watchamovie

import android.app.Application
import androidx.core.app.NotificationManagerCompat
import com.awkris.watchamovie.di.*
import com.awkris.watchamovie.utils.NotificationUtils
import com.facebook.stetho.Stetho
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import com.google.firebase.messaging.FirebaseMessaging
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
            modules(
                listOf(apiModule, dataStoreModule, repositoryModule, storageModule, viewmodelModule)
            )
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

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(object : OnCompleteListener<InstanceIdResult?> {
                override fun onComplete(task: Task<InstanceIdResult?>) {
                    if (!task.isSuccessful) {
                        Timber.w("getInstanceId failed")
                        return
                    }
                    // Get new Instance ID token
                    val token: String = task.result!!.token
                    Timber.d("New instance id token: %s", token)
                }
            })

        FirebaseMessaging.getInstance().subscribeToTopic("movie_release")
    }
}