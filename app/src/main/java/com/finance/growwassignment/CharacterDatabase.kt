package com.finance.growwassignment

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.finance.growwassignment.db.CharacterDao
import com.finance.growwassignment.db.RemoteKeysDao
import com.finance.growwassignment.models.CharacterRemoteKeys
import com.finance.growwassignment.models.Result
import com.finance.growwassignment.ui.screens.CharacterFragmentDirections
import com.finance.growwassignment.utilities.DateTypeConverter
import com.finance.growwassignment.utilities.StringListConverter

@Database(entities = [Result::class, CharacterRemoteKeys::class], version = 1, exportSchema = false)
@TypeConverters(StringListConverter::class, DateTypeConverter::class)
abstract class CharacterDatabase : RoomDatabase() {

    abstract fun characterDao(): CharacterDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}