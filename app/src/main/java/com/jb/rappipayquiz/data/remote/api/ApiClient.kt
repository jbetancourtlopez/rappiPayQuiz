package com.jb.rappipayquiz.data.remote.api

import com.jb.rappipayquiz.data.remote.interceptor.MovieInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private const val BASE_URL = "https://api.themoviedb.org/3/"
    const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w500"

    private val retrofit: Retrofit = Retrofit
        .Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(
            OkHttpClient.Builder()
            .addInterceptor(MovieInterceptor())
            .build())
        .build()

    fun movieService(): MovieApi = retrofit.create(MovieApi::class.java)

}