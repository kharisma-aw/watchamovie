package com.awkris.watchamovie.utils.service

import com.awkris.watchamovie.utils.NotificationUtils
import com.awkris.watchamovie.utils.storage.SessionSharedPreferences
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.koin.android.ext.android.get

class MovieFirebaseMessagingService() : FirebaseMessagingService() {
    private val sharedPreferences: SessionSharedPreferences = get()

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        if (message.data.isNotEmpty()) {
            generateNotification(message.data)
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        sharedPreferences.putToken(token)
    }

    private fun generateNotification(data: Map<String, String>) {
        val movieId = checkNotNull(data["id"]).toInt()
        val movieTitle = checkNotNull(data["title"])
        NotificationUtils.createReleaseNotification(applicationContext, movieId, movieTitle)
    }
}