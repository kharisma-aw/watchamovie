package com.awkris.watchamovie.data.api.utils

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class HeaderInterceptor : Interceptor{
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = request.newBuilder()
            .addHeader("Return-type", "text/json")
            .build()
        return chain.proceed(request)
    }
}