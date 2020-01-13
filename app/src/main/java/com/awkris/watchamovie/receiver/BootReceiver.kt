package com.awkris.watchamovie.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.awkris.watchamovie.data.repository.MovieDbRepository
import com.awkris.watchamovie.data.room.entity.Movie
import com.awkris.watchamovie.utils.NotificationUtils
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.koin.core.KoinComponent
import org.koin.core.get
import timber.log.Timber

class BootReceiver : BroadcastReceiver(), KoinComponent {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent?.action.equals("android.intent.action.BOOT_COMPLETED")) {
            get<MovieDbRepository>().getAllReminders()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    object : SingleObserver<List<Movie>> {
                        override fun onSuccess(t: List<Movie>) {
                            t.map {
                                NotificationUtils.scheduleAlarmsForReminder(context, it)
                            }
                        }

                        override fun onSubscribe(d: Disposable) {
                        }

                        override fun onError(e: Throwable) {
                            Timber.d("Error encountered:\n" +
                                    "cause: ${e.cause}\n" +
                                    "message: ${e.message}")
                        }

                    }
                )
        }
    }
}