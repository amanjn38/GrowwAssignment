package com.finance.growwassignment.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.finance.growwassignment.models.CharacterRemoteKeys

@Dao
interface RemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<CharacterRemoteKeys>)

    @Query("SELECT * FROM characterremotekeys WHERE id = :url")
    suspend fun getRemoteKeys(url: String): CharacterRemoteKeys?

    @Query("DELETE FROM characterremotekeys")
    suspend fun clearRemoteKeys()
}