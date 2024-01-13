package com.finance.growwassignment.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.finance.growwassignment.MovieDatabase
import com.finance.growwassignment.models.MovieResult

class CharacterMoviePagingSource(
    private val movies: List<String>,
    private val movieDatabase: MovieDatabase
) : PagingSource<Int, MovieResult>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieResult> {
        return try {
            val currentPage = params.key ?: 1
            val start = (currentPage - 1) * params.loadSize
            val end = start + params.loadSize
            val items = movies.subList(start.coerceAtLeast(0), end.coerceAtMost(movies.size))

            val movieResults = items.mapNotNull { url ->
                movieDatabase.getMovieDao().getMovieByUrl(url)
            }

            LoadResult.Page(
                data = movieResults,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (end >= movies.size) null else currentPage + 1
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieResult>): Int? {
        return state.anchorPosition
    }
}
