package com.awkris.watchamovie.utils.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.awkris.watchamovie.data.repository.MovieDbRepository
import com.awkris.watchamovie.utils.NotificationUtils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.get
import timber.log.Timber

class BootReceiver : BroadcastReceiver(), KoinComponent {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent?.action.equals("android.intent.action.BOOT_COMPLETED")) {
            GlobalScope.launch {
                try {
                    val reminders = get<MovieDbRepository>().getAllRemindersCoroutine()
                    reminders.map {
                        NotificationUtils.scheduleAlarmsForReminder(context, it)
                    }
                } catch (e: Exception) {
                    Timber.d("Error encountered:\n" +
                            "cause: ${e.cause}\n" +
                            "message: ${e.message}")
                }
            }
        }
    }
}