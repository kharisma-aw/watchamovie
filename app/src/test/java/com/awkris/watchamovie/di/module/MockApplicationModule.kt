package com.awkris.watchamovie.di.module

import com.awkris.watchamovie.data.utils.MockApiGenerator
import com.awkris.watchamovie.di.ActivityScope
import com.awkris.watchamovie.di.ApplicationScope
import com.google.gson.Gson
import dagger.Module
import dagger.Provides

@Module
class MockApplicationModule(private val baseUrl: String) {
    @Provides
    @ActivityScope
    fun provideMockApiGenerator(): MockApiGenerator {
        return MockApiGenerator(Gson(), baseUrl)
    }
}