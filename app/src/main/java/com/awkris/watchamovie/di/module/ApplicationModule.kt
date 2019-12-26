package com.awkris.watchamovie.di.module

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.awkris.watchamovie.data.api.utils.ApiFactory
import com.awkris.watchamovie.data.room.MovieDatabase
import com.awkris.watchamovie.di.ApplicationScope
import com.google.gson.Gson
import dagger.Module
import dagger.Provides

@Module
open class ApplicationModule(private val application: Application) {
    @Provides
    @ApplicationScope
    fun provideApplicationContext(): Context {
        return application
    }

    @Provides
    @ApplicationScope
    fun provideApiGenerator() = ApiFactory(Gson())

    @Provides
    @ApplicationScope
    fun provideMovieDatabase(context: Context): MovieDatabase {
        return Room.databaseBuilder(
            context,
            MovieDatabase::class.java,
            MovieDatabase.DB_NAME
        ).build()
    }
}