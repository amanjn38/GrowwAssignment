package com.finance.growwassignment.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.finance.growwassignment.utilities.StringListConverter

@Entity(tableName = "movies")
data class MovieResult(
    val title: String,
    val episode_id: Int,
    val opening_crawl: String,
    val director: String,
    val producer: String,
    val release_date: String,
    @TypeConverters(StringListConverter::class)
    val characters: List<String>,
    @TypeConverters(StringListConverter::class)
    val planets: List<String>,
    @TypeConverters(StringListConverter::class)
    val starships: List<String>,
    @TypeConverters(StringListConverter::class)
    val vehicles: List<String>,
    @TypeConverters(StringListConverter::class)
    val species: List<String>,
    val created: String,
    val edited: String,
    @PrimaryKey(autoGenerate = false)
    val url: String
)