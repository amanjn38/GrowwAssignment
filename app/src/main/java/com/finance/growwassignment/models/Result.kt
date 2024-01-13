package com.finance.growwassignment.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.finance.growwassignment.utilities.DateTypeConverter
import com.finance.growwassignment.utilities.StringListConverter

@Entity(tableName = "Characters")
data class Result(
    @ColumnInfo(name = "birth_year")
    val birth_year: String,

    @TypeConverters(DateTypeConverter::class)
    @ColumnInfo(name = "created")
    val created: String,

    @TypeConverters(DateTypeConverter::class)
    @ColumnInfo(name = "edited")
    val edited: String,

    @ColumnInfo(name = "eye_color")
    val eye_color: String,

    @TypeConverters(StringListConverter::class)
    @ColumnInfo(name = "films")
    val films: List<String>,

    @ColumnInfo(name = "gender")
    val gender: String,

    @ColumnInfo(name = "hair_color")
    val hair_color: String,

    @ColumnInfo(name = "height")
    val height: String,

    @ColumnInfo(name = "homeworld")
    val homeworld: String,

    @ColumnInfo(name = "mass")
    val mass: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "skin_color")
    val skin_color: String,

    @TypeConverters(StringListConverter::class)
    @ColumnInfo(name = "species")
    val species: List<String>,

    @TypeConverters(StringListConverter::class)
    @ColumnInfo(name = "starships")
    val starships: List<String>,

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "url")
    val url: String,

    @TypeConverters(StringListConverter::class)
    @ColumnInfo(name = "vehicles")
    val vehicles: List<String>
) : java.io.Serializable {
    // Optional: You can define additional functions or properties if needed
}
