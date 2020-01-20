package com.awkris.watchamovie.di.module

import com.awkris.watchamovie.data.api.MovieDbApi
import com.awkris.watchamovie.data.api.utils.ApiFactory
import com.awkris.watchamovie.di.ApplicationScope
import dagger.Module
import dagger.Provides

@Module
class ApiModule {
    @Provides
    @ApplicationScope
    fun provideMovieDbApi(apiFactory: ApiFactory): MovieDbApi {
        return apiFactory.createApi(MovieDbApi::class.java)
    }
}