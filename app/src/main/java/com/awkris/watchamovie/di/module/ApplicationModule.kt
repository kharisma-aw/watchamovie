package com.awkris.watchamovie.di.module

import android.app.Application
import android.content.Context
import com.awkris.watchamovie.data.api.utils.ApiFactory
import com.awkris.watchamovie.di.ApplicationScope
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
open class ApplicationModule(private val application: Application) {
    @Provides
    @ApplicationScope
    fun provideApplicationContext(): Context {
        return application
    }

    @Provides
    @Singleton
    fun provideApiGenerator() = ApiFactory(Gson())
}