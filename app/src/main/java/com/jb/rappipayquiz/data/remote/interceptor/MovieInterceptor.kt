package com.jb.rappipayquiz.data.remote.interceptor

import com.jb.rappipayquiz.BuildConfig
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response


class MovieInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val url: HttpUrl = chain.request().url().newBuilder()
            .addQueryParameter("api_key", BuildConfig.API_KEY)
            .build()

        val request = chain.request().newBuilder()
            .url(url)
            .build()

        return chain.proceed(request)
    }
}