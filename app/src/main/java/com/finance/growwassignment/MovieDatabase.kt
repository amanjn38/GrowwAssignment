package com.finance.growwassignment

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.finance.growwassignment.db.MovieDao
import com.finance.growwassignment.models.MovieResult
import com.finance.growwassignment.utilities.StringListConverter

@Database(entities = [MovieResult::class], version = 1, exportSchema = false)
@TypeConverters(StringListConverter::class)
abstract class MovieDatabase : RoomDatabase(){
    abstract fun getMovieDao(): MovieDao
}