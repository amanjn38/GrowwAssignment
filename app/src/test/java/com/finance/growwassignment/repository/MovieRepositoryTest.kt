package com.finance.growwassignment.repository

import com.finance.growwassignment.MovieDatabase
import com.finance.growwassignment.api.ApiService
import com.finance.growwassignment.db.MovieDao
import com.finance.growwassignment.models.MovieResult

import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.*
import retrofit2.Response

class MovieRepositoryTest {

    private val mockMovieDatabase = mock<MovieDatabase>()
    private val mockMovieDao = mock<MovieDao>()

    private val mockApiService = mock<ApiService>()

    private val movieRepository = MovieRepository(mockMovieDatabase, mockApiService)


    @Test
    fun `getCachedMovies should return movies from the database`() = runBlocking {
        // Given
        val expectedMovies = listOf(
            MovieResult(
                title = "Movie 1",
                episode_id = 1,
                opening_crawl = "Opening crawl 1",
                director = "Director 1",
                producer = "Producer 1",
                release_date = "2022-01-01",
                characters = listOf("Character 1", "Character 2"),
                planets = listOf("Planet 1", "Planet 2"),
                starships = listOf("Starship 1", "Starship 2"),
                vehicles = listOf("Vehicle 1", "Vehicle 2"),
                species = listOf("Species 1", "Species 2"),
                created = "2022-01-01T00:00:00Z",
                edited = "2022-01-01T00:00:00Z",
                url = "https://example.com/movie1"
            ),
            MovieResult(
                title = "Movie 2",
                episode_id = 2,
                opening_crawl = "Opening crawl 2",
                director = "Director 2",
                producer = "Producer 2",
                release_date = "2022-02-01",
                characters = listOf("Character 3", "Character 4"),
                planets = listOf("Planet 3", "Planet 4"),
                starships = listOf("Starship 3", "Starship 4"),
                vehicles = listOf("Vehicle 3", "Vehicle 4"),
                species = listOf("Species 3", "Species 4"),
                created = "2022-02-01T00:00:00Z",
                edited = "2022-02-01T00:00:00Z",
                url = "https://example.com/movie2"
            )
        )
        whenever(mockMovieDatabase.getMovieDao()).thenReturn(mockMovieDao)
        whenever(mockMovieDao.getAllMovies()).thenReturn(expectedMovies)
        // When
        val result = movieRepository.getCachedMovies()

        // Then
        assertEquals(expectedMovies, result)
    }

    @Test
    fun `fetchAndCacheMovies should handle API error`() = runBlocking {
        val mockMovieDao = mock<MovieDao>()
        whenever(mockMovieDatabase.getMovieDao()).thenReturn(mockMovieDao)

        // Mock API error response
        whenever(mockApiService.getMovies(1)).thenReturn(Response.error(404, "".toResponseBody()))

        // When
        val result = movieRepository.fetchAndCacheMovies()

        // Then
        assertEquals(emptyList<MovieResult>(), result)

        verify(mockMovieDao, never()).insertMovies(any())
    }

}
