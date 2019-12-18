package com.awkris.watchamovie.data.api.utils

import com.awkris.watchamovie.BuildConfig
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiFactory(gson: Gson) {
    private val retrofit: Retrofit

    init {
        retrofit = initRetrofit(gson, getOkHttpClient())
    }

    fun <A> createApi(apiClass: Class<A>): A {
        return retrofit.create(apiClass)
    }

    private fun initRetrofit(
        gson: Gson,
        client: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(UrlConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
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
                    addNetworkInterceptor(StethoInterceptor())
                }
            }
        return okHttpClientBuilder.build()
    }

    companion object {
        private const val CONNECTION_TIMEOUT = 60
    }
}