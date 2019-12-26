package com.awkris.watchamovie.di.component

import android.content.Context
import com.awkris.watchamovie.data.repository.MovieDbRepository
import com.awkris.watchamovie.data.room.MovieDatabase
import com.awkris.watchamovie.di.ApplicationScope
import com.awkris.watchamovie.di.module.ApiModule
import com.awkris.watchamovie.di.module.ApplicationModule
import com.awkris.watchamovie.presentation.moviedetail.MovieDetailActivity
import com.awkris.watchamovie.presentation.nowplaying.NowPlayingActivity
import dagger.Component

@ApplicationScope
@Component(modules = [
    ApplicationModule::class,
    ApiModule::class
])
interface ApplicationComponent {
    fun applicationContext(): Context
    fun inject(activity: MovieDetailActivity)
    fun inject(activity: NowPlayingActivity)
    fun movieDb(): MovieDatabase
    fun repository(): MovieDbRepository
}