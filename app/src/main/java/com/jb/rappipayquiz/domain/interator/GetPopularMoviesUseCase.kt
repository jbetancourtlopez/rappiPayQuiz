package com.jb.rappipayquiz.domain.interator

import com.jb.rappipayquiz.data.MovieRepository
import com.jb.rappipayquiz.domain.model.Movie
import com.jb.rappipayquiz.domain.model.MoviesResponse

class GetPopularMoviesUseCase(private val repository: MovieRepository) {

    suspend operator fun invoke(page: Int): MoviesResponse {

        val movies = repository.getPopularMoviesRemote(page)

        if(movies != null && movies.results.size > 0){
            repository.insertAllMovies(movies.results, 0)
            return movies
        }else{

            val movieslocal = repository.getPopularMoviesLocal(page, 0)
            return movieslocal
        }
    }
}