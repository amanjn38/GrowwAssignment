package com.finance.growwassignment.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finance.growwassignment.MovieDatabase
import com.finance.growwassignment.models.MovieResult
import com.finance.growwassignment.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(private val movieDatabase: MovieDatabase, private val repository: MovieRepository) : ViewModel() {

    private val _movies = MutableLiveData<List<MovieResult>>()
    val movies: LiveData<List<MovieResult>> get() = _movies

    init {
        // Initialize the ViewModel with existing data from the Room Database
        viewModelScope.launch {
            _movies.value = repository.getCachedMovies()
        }
    }

    fun getMovies() {
        viewModelScope.launch {
            // Check if movies are already in the Room Database
            val cachedMovies = repository.getCachedMovies()

            if (cachedMovies.isEmpty()) {
                // If no movies in the database, fetch from the API
                _movies.value = repository.fetchAndCacheMovies()
            } else {
                // If movies are already in the database, use them
                _movies.value = cachedMovies
            }
        }
    }
}