package com.jb.rappipayquiz.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jb.rappipayquiz.data.local.dao.MovieDao
import com.jb.rappipayquiz.data.local.entities.MovieEntity


@Database(entities = [MovieEntity::class], version = 5)
abstract class MoviesDatabase: RoomDatabase() {

    abstract fun getMovieDao():MovieDao

    companion object {
        @Volatile private var instance: MoviesDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context)= instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also { instance = it}
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context,
            MoviesDatabase::class.java, "movies.db")
            .build()
    }
}