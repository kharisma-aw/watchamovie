package com.awkris.watchamovie

import com.awkris.watchamovie.di.component.DaggerMockApplicationComponent
import com.awkris.watchamovie.di.component.MockApplicationComponent
import com.awkris.watchamovie.di.module.MockApplicationModule
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
abstract class MockitoTest {
    protected lateinit var baseUrl: String
    private lateinit var injector: MockApplicationComponent

    @Before
    @Throws(Exception::class)
    open fun setUp() {
        injector = DaggerMockApplicationComponent.builder()
            .mockApplicationModule(MockApplicationModule(baseUrl))
            .build()
    }

    protected fun <T> createApi(tClass: Class<T>): T {
        return injector.mockApiGenerator().createApi(tClass)
    }
}