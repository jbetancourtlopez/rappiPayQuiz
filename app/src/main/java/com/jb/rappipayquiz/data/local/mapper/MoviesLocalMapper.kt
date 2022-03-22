package com.jb.rappipayquiz.data.local.mapper

import com.jb.rappipayquiz.core.orDefault
import com.jb.rappipayquiz.core.orFalse
import com.jb.rappipayquiz.data.local.entities.MovieEntity
import com.jb.rappipayquiz.domain.model.Movie

class MoviesLocalMapper {

    fun mapFromLocal(movieEntity: MovieEntity): Movie {
        return Movie(movieEntity.popularity.orDefault(), movieEntity.vote_count.orDefault(), movieEntity.video.orFalse(),
            movieEntity.poster_path.orEmpty(), movieEntity.id, movieEntity.adult.orFalse(), movieEntity.backdrop_path.orEmpty(),
            movieEntity.original_language.orEmpty(), movieEntity.original_title.orEmpty(), movieEntity.title,
            movieEntity.vote_average.orDefault(), movieEntity.overview.orEmpty(), movieEntity.release_date.orEmpty())
    }

    fun mapToLocal(movie: Movie, category: Int): MovieEntity {
        return MovieEntity(movie.id, category, movie.popularity, movie.vote_count, movie.video, movie.poster_path, movie.adult,
            movie.backdrop_path, movie.original_language, movie.original_title, movie.title, movie.vote_average,
            movie.overview, movie.release_date)
    }

}