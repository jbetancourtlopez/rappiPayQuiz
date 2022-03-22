package com.jb.rappipayquiz.data

import android.content.Context
import androidx.room.RoomDatabase
import com.jb.rappipayquiz.data.local.MoviesDatabase
import com.jb.rappipayquiz.data.local.dao.MovieDao
import com.jb.rappipayquiz.data.local.entities.MovieEntity
import com.jb.rappipayquiz.data.local.mapper.MoviesLocalMapper
import com.jb.rappipayquiz.data.remote.api.ApiClient
import com.jb.rappipayquiz.data.remote.mapper.MoviesRemoteMapper
import com.jb.rappipayquiz.data.remote.model.MovieRemoteResponse
import com.jb.rappipayquiz.data.remote.service.MovieService
import com.jb.rappipayquiz.domain.model.Movie
import com.jb.rappipayquiz.domain.model.MovieDetail
import com.jb.rappipayquiz.domain.model.MoviesResponse

class MovieRepository(private val context: Context) {

    private val moviesRemoteMapper = MoviesRemoteMapper()
    private val moviesLocalMapper = MoviesLocalMapper()

    private val movieApi = MovieService()
    private val movieDao = MoviesDatabase.invoke(context).getMovieDao()

    // Remote
    suspend fun getPopularMoviesRemote(page: Int): MoviesResponse {
        val response = movieApi.getPopularMovies(page)
        var model = moviesRemoteMapper.mapFromRemote(response)
        return model
    }

    suspend fun getTopRateMoviesRemote(page: Int): MoviesResponse {
        val response = movieApi.getTopRateMovies(page)
        var model = moviesRemoteMapper.mapFromRemote(response)
        return model
    }

    suspend fun getSearchMoviesRemote(page: Int, query: String): MoviesResponse {
        val response = movieApi.getSearch(page, query)
        var model = moviesRemoteMapper.mapFromRemote(response)
        return model
    }

    suspend fun getMovieRemote(id: String): MovieDetail? {
        val response = movieApi.getMovie(id)
        if(response != null){
            var model = moviesRemoteMapper.mapDetailFromRemote(response!!)
            return model
        }
        return null

    }

    // Local
    suspend fun insertAllMovies(movies: List<Movie>, category: Int) {

        var lst_movieEntity = movies.map {
            moviesLocalMapper.mapToLocal(it, category)
        }
        movieDao.insertAllMovies(lst_movieEntity)
    }

    suspend fun getPopularMoviesLocal(page: Int, category: Int): MoviesResponse {

        val tmp = movieDao.getMovies(page, category)
        val lst_movies: List<Movie> = tmp.map {
            moviesLocalMapper.mapFromLocal(it)
        }
        var movie = MoviesResponse(0,0,0, lst_movies)

        return movie
    }

    suspend fun getTopRateMoviesLocal(page: Int, category: Int): MoviesResponse {

        val tmp = movieDao.getMovies(page, category)
        val lst_movies: List<Movie> = tmp.map {
            moviesLocalMapper.mapFromLocal(it)
        }
        var movie = MoviesResponse(0,0,0, lst_movies)

        return movie
    }

    suspend fun getSearchMoviesLocal(page: Int, category: Int, query:String): MoviesResponse {

        val tmp = movieDao.searchListMovie(query, page)
        val lst_movies: List<Movie> = tmp.map {
            moviesLocalMapper.mapFromLocal(it)
        }
        var movie = MoviesResponse(0,0,0, lst_movies)

        return movie
    }



    /*
    fun getMovieLocal(id: String): MovieDetail {
        val response = movieApi.getMovie(id)
        var model = moviesRemoteMapper.mapDetailFromRemote(response)
        return model
    }*/


}