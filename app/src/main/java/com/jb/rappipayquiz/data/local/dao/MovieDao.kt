package com.jb.rappipayquiz.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.jb.rappipayquiz.data.local.entities.MovieEntity

@Dao
interface MovieDao {

    @Query("SELECT * FROM movies WHERE category=:category LIMIT (:page*20)")
    suspend fun getMovies(page: Int, category: Int):List<MovieEntity>

    @Query("SELECT * FROM movies WHERE title LIKE '%' || :query || '%'  LIMIT (:pageNumber*20) ")
    suspend fun searchListMovie(query: String,pageNumber:Int): List<MovieEntity>

    @Query("SELECT * FROM movies WHERE id == :id")
    suspend fun getMovie(id: Int): MovieEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllMovies(movies:List<MovieEntity>)

    @Query("DELETE FROM movies")
    suspend fun deleteAllMovies()

}