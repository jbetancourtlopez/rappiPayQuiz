package com.jb.rappipayquiz.data.remote.api

import androidx.lifecycle.LiveData
import com.jb.rappipayquiz.data.remote.model.MovieDetailRemote
import com.jb.rappipayquiz.data.remote.model.MovieRemoteResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {

    @GET("movie/popular")
    suspend fun getPopular(@Query("page") page: Int): Response<MovieRemoteResponse>

    @GET("movie/top_rated")
    suspend fun getTopRate(@Query("page") page: Int): Response<MovieRemoteResponse>

    @GET("movie/{id}")
    suspend fun getMovie(@Path("id") id: String): Response<MovieDetailRemote>

    @GET("search/movie")
    suspend fun searchMovies(@Query("query") query: String? = null, @Query("page") page: Int = 1, ):  Response<MovieRemoteResponse>


}