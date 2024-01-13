package com.finance.growwassignment.api

import com.finance.growwassignment.models.CharacterResponse
import com.finance.growwassignment.models.Movies
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface ApiService {
    @GET("people/")
    suspend fun getCharacters(
        @Query("page") page: Int
    ): Response<CharacterResponse>

    @GET("films/")
    suspend fun getMovies(@Query("page") page: Int): Response<Movies>
    abstract fun searchMovies(query: String, nextPage: Int): Any

}