package com.awkris.watchamovie.di.component

import com.awkris.watchamovie.data.utils.MockApiGenerator
import com.awkris.watchamovie.di.ActivityScope
import com.awkris.watchamovie.di.ApplicationScope
import com.awkris.watchamovie.di.module.MockApplicationModule
import dagger.Component

@ActivityScope
@Component(
//    dependencies = [ApplicationComponent::class],
    modules = [MockApplicationModule::class]
)
interface MockApplicationComponent {
    fun mockApiGenerator(): MockApiGenerator
}