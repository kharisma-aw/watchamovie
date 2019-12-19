package com.awkris.watchamovie.data.utils

import com.awkris.watchamovie.data.api.utils.HeaderInterceptor
import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MockApiGenerator(gson: Gson, baseUrl: String) {
    private val retrofit: Retrofit

    init {
        retrofit = initRetrofit(gson, baseUrl)
    }

    fun <A> createApi(apiClass: Class<A>): A {
        return retrofit.create(apiClass)
    }

    private fun initRetrofit(gson: Gson, baseUrl: String): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(HeaderInterceptor())
                    .build()
            )
            .build()
    }
}