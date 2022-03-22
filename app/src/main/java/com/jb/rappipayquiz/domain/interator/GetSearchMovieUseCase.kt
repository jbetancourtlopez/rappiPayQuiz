package com.jb.rappipayquiz.domain.interator

import com.jb.rappipayquiz.data.MovieRepository
import com.jb.rappipayquiz.domain.model.MoviesResponse

class GetSearchMovieUseCase(private val repository: MovieRepository) {

    suspend operator fun invoke(page: Int, query: String): MoviesResponse {

        val movies = repository.getSearchMoviesRemote(page, query)

        if(movies != null && movies.results.size > 0){
            repository.insertAllMovies(movies.results, 2)
            return movies
        }else{
            val movieslocal = repository.getSearchMoviesLocal (page, 2, query)
            return movieslocal
        }
    }
}