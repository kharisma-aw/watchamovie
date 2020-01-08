package com.awkris.watchamovie.utils

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.awkris.watchamovie.R
import com.awkris.watchamovie.data.model.response.MovieDetailResponse
import com.awkris.watchamovie.data.room.entity.Movie
import com.awkris.watchamovie.presentation.main.MainActivity
import com.awkris.watchamovie.presentation.moviedetail.MovieDetailActivity
import com.awkris.watchamovie.receiver.AlarmReceiver
import java.util.*

object NotificationUtils {
    fun isReminderSet(context: Context, movieId: Int): Boolean {
        val intent = Intent(context.applicationContext, AlarmReceiver::class.java).apply {
            action = context.getString(R.string.action_movierelease_reminder)
            type = "$movieId"
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            movieId,
            intent,
            PendingIntent.FLAG_NO_CREATE
        )
        return pendingIntent != null
    }

    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    fun createNotificationChannel(
        context: Context,
        importance: Int,
        name: String,
        description: String
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "${context.packageName}-$name"
            val channel = NotificationChannel(channelId, name, importance)
            channel.description = description
            channel.setShowBadge(false)

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun createReleaseNotification(
        context: Context,
        movieId: Int,
        movieTitle: String
    ) {
        val channelId = "${context.packageName}-${context.getString(R.string.app_name)}"
        val notificationBuilder = NotificationCompat.Builder(context, channelId).apply {
            setSmallIcon(R.mipmap.ic_launcher_round)
            setContentTitle(movieTitle)
            setContentText(
                context.resources.getString(
                    R.string.notification_release_content,
                    movieTitle
                )
            )
            setAutoCancel(true)
            setStyle(NotificationCompat.BigTextStyle().bigText(
                context.resources.getString(
                    R.string.notification_release_content,
                    movieTitle
                )
            ))
            priority = NotificationCompat.PRIORITY_DEFAULT

            val pendingIntent = createPendingIntentForMovieDetail(context, movieId)
            setContentIntent(pendingIntent)
        }

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(movieId, notificationBuilder.build())
    }

    fun deleteAlarmsForReminder(
        context: Context,
        movieDetail: MovieDetailResponse
    ) {
        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = createPendingIntent(context, movieDetail.id, movieDetail.title)
        alarmMgr.cancel(alarmIntent)
    }

    fun scheduleAlarmsForReminder(
        context: Context,
        movieDetail: MovieDetailResponse
    ) {
        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = createPendingIntent(context, movieDetail.id, movieDetail.title)
        scheduleAlarm(movieDetail.releaseDate, alarmIntent, alarmMgr)
    }

    fun scheduleAlarmsForReminder(
        context: Context,
        movie: Movie
    ) {
        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = createPendingIntent(context, movie.id, movie.title)
        scheduleAlarm(movie.releaseDate, alarmIntent, alarmMgr)
    }

    private fun scheduleAlarm(
        date: String,
        alarmIntent: PendingIntent,
        alarmMgr: AlarmManager
    ) {
        val releaseDate = formatDate(date)

        val datetimeToAlarm = Calendar.getInstance(Locale.getDefault())
        datetimeToAlarm.timeInMillis = System.currentTimeMillis()
        datetimeToAlarm.set(Calendar.HOUR_OF_DAY, 9)
        datetimeToAlarm.set(Calendar.MINUTE, 0)
        datetimeToAlarm.set(Calendar.SECOND, 0)
        datetimeToAlarm.set(formatReleaseYear(date).toInt(), releaseDate.month, releaseDate.date)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmMgr.setExact(AlarmManager.RTC_WAKEUP, datetimeToAlarm.timeInMillis, alarmIntent)
        } else {
            alarmMgr.set(AlarmManager.RTC_WAKEUP, datetimeToAlarm.timeInMillis, alarmIntent)

        }
    }

    private fun createPendingIntent(
        context: Context,
        movieId: Int,
        movieTitle: String
    ): PendingIntent {
        val intent = Intent(context.applicationContext, AlarmReceiver::class.java).apply {
            action = context.getString(R.string.action_movierelease_reminder)
            type = "$movieId"
            putExtra(MovieDetailActivity.MOVIE_ID, movieId)
            putExtra(MovieDetailActivity.MOVIE_TITLE, movieTitle)
        }
        return PendingIntent.getBroadcast(
            context,
            movieId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun createPendingIntentForMovieDetail(context: Context, movieId: Int): PendingIntent? {
        val intent = MainActivity.newIntent(context, movieId).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        return PendingIntent.getActivity(context, movieId, intent, 0)
    }
}