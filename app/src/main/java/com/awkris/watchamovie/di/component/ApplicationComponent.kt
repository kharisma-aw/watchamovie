package com.awkris.watchamovie.di.component

import android.content.Context
import com.awkris.watchamovie.data.repository.MovieDbRepository
import com.awkris.watchamovie.di.ApplicationScope
import com.awkris.watchamovie.di.module.ApiModule
import com.awkris.watchamovie.di.module.ApplicationModule
import com.awkris.watchamovie.di.module.MainActivityModule
import com.awkris.watchamovie.presentation.main.MainActivity
import com.awkris.watchamovie.presentation.moviedetail.MovieDetailActivity
import com.awkris.watchamovie.presentation.nowplaying.NowPlayingDataSource
import dagger.Component
import dagger.android.AndroidInjectionModule

@ApplicationScope
@Component(modules = [
    AndroidInjectionModule::class,
    ApplicationModule::class,
    ApiModule::class,
    MainActivityModule::class
])
interface ApplicationComponent {
    fun inject(activity: MainActivity)
    fun inject(activity: MovieDetailActivity)

    fun applicationContext(): Context
    fun nowPlayingDataSource(): NowPlayingDataSource
    fun repository(): MovieDbRepository
}