package com.jb.rappipayquiz.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey



@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey val id : Int,
    @ColumnInfo(name = "category") val category : Int = 0,
    @ColumnInfo(name = "popularity") val popularity : Double?,
    @ColumnInfo(name = "vote_count") val vote_count : Int?,
    @ColumnInfo(name = "video") val video : Boolean?,
    @ColumnInfo(name = "poster_path") val poster_path : String?,
    @ColumnInfo(name = "adult") val adult : Boolean?,
    @ColumnInfo(name = "backdrop_path") val backdrop_path : String?,
    @ColumnInfo(name = "original_language") val original_language : String?,
    @ColumnInfo(name = "original_title") val original_title : String?,
    @ColumnInfo(name = "title") val title : String,
    @ColumnInfo(name = "vote_average") val vote_average : Double?,
    @ColumnInfo(name = "overview") val overview : String?,
    @ColumnInfo(name = "release_date") val release_date : String?
)