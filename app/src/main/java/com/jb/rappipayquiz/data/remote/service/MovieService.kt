package com.jb.rappipayquiz.data.remote.service

import com.jb.rappipayquiz.data.remote.api.ApiClient
import com.jb.rappipayquiz.data.remote.model.MovieDetailRemote
import com.jb.rappipayquiz.data.remote.model.MovieRemoteResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieService {

    private val api = ApiClient.movieService()

    suspend fun getPopularMovies(page: Int): MovieRemoteResponse {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getPopular(page)
                return@withContext response.body() ?: MovieRemoteResponse(0, 0, 0, emptyList())

            } catch (throwable: Throwable) {
                return@withContext MovieRemoteResponse(0, 0, 0, emptyList())
            }
        }
    }

    suspend fun getTopRateMovies(page: Int): MovieRemoteResponse {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getTopRate(page)
                return@withContext response.body() ?: MovieRemoteResponse(0, 0, 0, emptyList())

            } catch (throwable: Throwable) {
                return@withContext MovieRemoteResponse(0, 0, 0, emptyList())
            }
        }
    }

    suspend fun getMovie(id: String): MovieDetailRemote? {

        return withContext(Dispatchers.IO) {
            try {
                var response = api.getMovie(id)
                return@withContext response.body() ?: null

            } catch (throwable: Throwable) {
                return@withContext null
            }
        }
    }

    suspend fun getSearch(id: Int, query: String): MovieRemoteResponse {
        return withContext(Dispatchers.IO) {
            try {
                var response = api.searchMovies(query, id)
                return@withContext response.body() ?: MovieRemoteResponse(0, 0, 0, emptyList())

            } catch (throwable: Throwable) {
                return@withContext MovieRemoteResponse(0, 0, 0, emptyList())
            }
        }
    }


}