package com.finance.growwassignment.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.finance.growwassignment.models.Result

@Dao
interface CharacterDao {

    @Query("SELECT * FROM characters")
    fun getCharacters(): PagingSource<Int, Result>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCharacters(characters: List<Result>)

    @Query("DELETE FROM characters")
    suspend fun deleteCharacters()

    @Query("SELECT * FROM characters ORDER BY name ASC")
    fun getCharactersSortedByName(): PagingSource<Int, Result>

    @Query("SELECT * FROM characters ORDER BY height ASC")
    fun getCharactersSortedByHeight(): PagingSource<Int, Result>

    @Query("SELECT * FROM characters ORDER BY mass ASC")
    fun getCharactersSortedByMass(): PagingSource<Int, Result>

    @Query("SELECT * FROM characters ORDER BY created ASC")
    fun getCharactersSortedByCreated(): PagingSource<Int, Result>

    @Query("SELECT * FROM characters ORDER BY edited ASC")
    fun getCharactersSortedByEdited(): PagingSource<Int, Result>

    @Query("SELECT * FROM characters WHERE eye_color = :hairColor")
    fun getCharactersByHairColor(
        hairColor: String
    ): PagingSource<Int, Result>

    @Query("SELECT * FROM characters WHERE height BETWEEN :minHeight AND :maxHeight")
    fun getCharactersFilterByHeightRange(
        minHeight: String,
        maxHeight: String
    ): PagingSource<Int, Result>


    @Query("SELECT * FROM characters WHERE mass BETWEEN :minMass AND :maxMass")
    fun getCharactersFilterByMassRange(
        minMass: String,
        maxMass: String
    ): PagingSource<Int, Result>

}