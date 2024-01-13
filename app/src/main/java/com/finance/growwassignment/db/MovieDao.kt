package com.finance.growwassignment.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.finance.growwassignment.models.MovieResult

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieResult>)

    @Query("SELECT * FROM movies")
    fun getAllMovies(): List<MovieResult>

    @Query("SELECT * FROM movies WHERE url = :url")
    suspend fun getMovieByUrl(url: String): MovieResult?
}
