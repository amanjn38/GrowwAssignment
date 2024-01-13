package com.finance.growwassignment.repository

import androidx.lifecycle.LiveData
import com.finance.growwassignment.MovieDatabase
import com.finance.growwassignment.api.ApiService
import com.finance.growwassignment.db.MovieDao
import com.finance.growwassignment.models.MovieResult
import com.finance.growwassignment.models.Movies
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieRepository(
    private val movieDatabase: MovieDatabase,
    private val apiService: ApiService
) {
    suspend fun getCachedMovies(): List<MovieResult> {
        return withContext(Dispatchers.IO) {
            movieDatabase.getMovieDao().getAllMovies()
        }
    }

    suspend fun fetchAndCacheMovies(): List<MovieResult> {
        val response = apiService.getMovies(1)

        if (response.isSuccessful) {
            val movies = response.body()?.results
            if (movies != null) {
                movieDatabase.getMovieDao().insertMovies(movies)
            }
        } else {
            // Handle API error
            return emptyList()
        }

        return emptyList()
    }
}