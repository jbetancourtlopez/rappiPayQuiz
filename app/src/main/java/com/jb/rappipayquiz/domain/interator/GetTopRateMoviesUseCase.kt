package com.jb.rappipayquiz.domain.interator

import com.jb.rappipayquiz.data.MovieRepository
import com.jb.rappipayquiz.domain.model.MoviesResponse



class GetTopRateMoviesUseCase(private val repository: MovieRepository) {

    suspend operator fun invoke(page: Int): MoviesResponse {

        val movies = repository.getTopRateMoviesRemote(page)

        if(movies != null && movies.results.size > 0){
            repository.insertAllMovies(movies.results, 1)
            return movies
        }else{
            val movieslocal = repository.getTopRateMoviesLocal(page, 1)
            return movieslocal
        }
    }
}