package com.finance.growwassignment.viewmodels

import com.finance.growwassignment.MovieDatabase
import com.finance.growwassignment.models.MovieResult
import com.finance.growwassignment.repository.MovieRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MovieViewModelTest {

    // Mocks for dependencies
    @Mock
    private lateinit var movieDatabase: MovieDatabase

    @Mock
    private lateinit var repository: MovieRepository

    // The ViewModel to be tested
    private lateinit var movieViewModel: MovieViewModel


    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        movieViewModel = MovieViewModel(movieDatabase, repository)
    }

    @Test
    fun `getMovies should fetch movies from the repository when the database is empty`() =
        runBlocking {
            val emptyCachedMovies: List<MovieResult> = emptyList()
            val apiMovies = listOf(
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

            `when`(repository.getCachedMovies()).thenReturn(emptyCachedMovies)
            `when`(repository.fetchAndCacheMovies()).thenReturn(apiMovies)

            movieViewModel.getMovies()

            verify(repository).getCachedMovies()
            verify(repository).fetchAndCacheMovies()

            assertEquals(apiMovies, movieViewModel.movies.value)
        }

    @Test
    suspend fun `getMovies should use cached movies when the database is not empty`() {
        // Given
        val cachedMovies = listOf(
            MovieResult(
                title = "Cached Movie 1",
                episode_id = 10,
                opening_crawl = "Cached Opening crawl 1",
                director = "Cached Director 1",
                producer = "Cached Producer 1",
                release_date = "2021-12-01",
                characters = listOf("Cached Character 1", "Cached Character 2"),
                planets = listOf("Cached Planet 1", "Cached Planet 2"),
                starships = listOf("Cached Starship 1", "Cached Starship 2"),
                vehicles = listOf("Cached Vehicle 1", "Cached Vehicle 2"),
                species = listOf("Cached Species 1", "Cached Species 2"),
                created = "2021-12-01T00:00:00Z",
                edited = "2021-12-01T00:00:00Z",
                url = "https://example.com/cached_movie1"
            ),
            MovieResult(
                title = "Cached Movie 2",
                episode_id = 20,
                opening_crawl = "Cached Opening crawl 2",
                director = "Cached Director 2",
                producer = "Cached Producer 2",
                release_date = "2021-11-01",
                characters = listOf("Cached Character 3", "Cached Character 4"),
                planets = listOf("Cached Planet 3", "Cached Planet 4"),
                starships = listOf("Cached Starship 3", "Cached Starship 4"),
                vehicles = listOf("Cached Vehicle 3", "Cached Vehicle 4"),
                species = listOf("Cached Species 3", "Cached Species 4"),
                created = "2021-11-01T00:00:00Z",
                edited = "2021-11-01T00:00:00Z",
                url = "https://example.com/cached_movie2"
            )
            // Add more MovieResult objects as needed
        )

        val apiMovies = listOf(
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


        `when`(repository.getCachedMovies()).thenReturn(cachedMovies)
        `when`(repository.fetchAndCacheMovies()).thenReturn(apiMovies)

        movieViewModel.getMovies()

        verify(repository).getCachedMovies()
        verify(repository, never()).fetchAndCacheMovies()

        assertEquals(cachedMovies, movieViewModel.movies.value)
    }


}
