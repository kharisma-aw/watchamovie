package com.awkris.watchamovie.data.api.utils

import com.awkris.watchamovie.BuildConfig
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class ApiFactory(moshi: Moshi) {
    private val retrofit: Retrofit

    init {
        retrofit = initRetrofit(moshi, getOkHttpClient())
    }

    fun <A> createApi(apiClass: Class<A>): A {
        return retrofit.create(apiClass)
    }

    private fun initRetrofit(
        moshi: Moshi,
        client: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(UrlConstants.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build()
    }

    private fun getOkHttpClient(): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()
            .addInterceptor(HeaderInterceptor())
            .connectTimeout(CONNECTION_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .writeTimeout(CONNECTION_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .readTimeout(CONNECTION_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .apply {
                if (BuildConfig.BUILD_TYPE.contentEquals("debug")) {
                    val loggingInterceptor = HttpLoggingInterceptor()
                    loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                    addInterceptor(loggingInterceptor)
                    addNetworkInterceptor(StethoInterceptor())
                }
            }
        return okHttpClientBuilder.build()
    }

    companion object {
        private const val CONNECTION_TIMEOUT = 60
    }
}