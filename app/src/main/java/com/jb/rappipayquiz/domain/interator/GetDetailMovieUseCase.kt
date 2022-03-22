package com.jb.rappipayquiz.domain.interator

import com.jb.rappipayquiz.data.MovieRepository
import com.jb.rappipayquiz.domain.model.MovieDetail
import com.jb.rappipayquiz.domain.model.MoviesResponse


class GetDetailMovieUseCase(private val repository: MovieRepository) {

    suspend operator fun invoke(movie_id: Int): MovieDetail? {
        val movie = repository.getMovieRemote(movie_id.toString())
        if(movie != null){
            return movie
        }
        return null
    }
}