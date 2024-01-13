package com.finance.growwassignment.di

import android.content.Context
import androidx.room.Room
import com.finance.growwassignment.CharacterDatabase
import com.finance.growwassignment.MovieDatabase
import com.finance.growwassignment.api.ApiService
import com.finance.growwassignment.repository.CharacterRepository
import com.finance.growwassignment.repository.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideApiService(okHttpClient: OkHttpClient): ApiService {
        return Retrofit.Builder()
            .baseUrl("https://swapi.dev/api/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideCharacterRepository(apiService: ApiService, characterDatabase: CharacterDatabase): CharacterRepository {
        return CharacterRepository(apiService, characterDatabase)
    }

    @Singleton
    @Provides
    fun provideMovieRepository(apiService: ApiService, movieDatabase: MovieDatabase): MovieRepository {
        return MovieRepository(movieDatabase, apiService)
    }

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) : CharacterDatabase {
        return Room.databaseBuilder(context, CharacterDatabase::class.java,"characterDB").build()
    }

    @Singleton
    @Provides
    fun provideMovieDatabase(@ApplicationContext context: Context) : MovieDatabase {
        return Room.databaseBuilder(context, MovieDatabase::class.java,"movieDB").build()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS) // Connection timeout
            .readTimeout(30, TimeUnit.SECONDS)    // Read timeout
            // Add other OkHttpClient configurations if needed
            .build()
    }
}