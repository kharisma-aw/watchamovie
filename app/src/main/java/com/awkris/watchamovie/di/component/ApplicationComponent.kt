package com.awkris.watchamovie.di.component

import android.content.Context
import com.awkris.watchamovie.di.ApplicationScope
import com.awkris.watchamovie.di.module.ApiModule
import com.awkris.watchamovie.di.module.ApplicationModule
import com.awkris.watchamovie.di.module.DataStoreModule
import dagger.Component

@ApplicationScope
@Component(modules = [
    ApplicationModule::class,
    ApiModule::class,
    DataStoreModule::class
])
interface ApplicationComponent {
    fun getApplicationContext(): Context
}