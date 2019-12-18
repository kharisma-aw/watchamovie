package com.awkris.watchamovie.di.module

import com.awkris.watchamovie.data.api.MovieDbApi
import com.awkris.watchamovie.data.datastore.CloudMovieDataStore
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataStoreModule {
    @Provides
    @Singleton
    fun provideCloudDataStore(api: MovieDbApi): CloudMovieDataStore {
        return CloudMovieDataStore(api)
    }
}