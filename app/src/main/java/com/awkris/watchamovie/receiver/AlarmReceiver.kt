package com.awkris.watchamovie.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.awkris.watchamovie.R
import com.awkris.watchamovie.presentation.moviedetail.MovieDetailFragment
import com.awkris.watchamovie.utils.NotificationUtils

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent != null && intent.action != null) {
            if (intent.action!!.equals(context.getString(R.string.action_movierelease_reminder), ignoreCase = true)) {
                if (intent.extras != null) {
                    val bundle = checkNotNull(intent.extras)
                    val movieId = bundle.getInt(MovieDetailFragment.MOVIE_ID)
                    val movieTitle = bundle.getString(MovieDetailFragment.MOVIE_TITLE)
                    if (movieId != 0 && !movieTitle.isNullOrEmpty()) {
                        NotificationUtils.createReleaseNotification(context, movieId, movieTitle)
                    }
                }
            }
        }
    }
}