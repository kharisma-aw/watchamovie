package com.awkris.watchamovie.di.module

import com.awkris.watchamovie.data.api.MovieDbApi
import com.awkris.watchamovie.data.api.utils.ApiFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApiModule {
    @Provides
    @Singleton
    fun provideMovieDbApi(apiFactory: ApiFactory): MovieDbApi {
        return apiFactory.createApi(MovieDbApi::class.java)
    }
}