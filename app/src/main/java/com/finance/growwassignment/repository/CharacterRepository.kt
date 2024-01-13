package com.finance.growwassignment.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.finance.growwassignment.CharacterDatabase
import com.finance.growwassignment.api.ApiService
import com.finance.growwassignment.paging.CharacterRemoteMediator
import javax.inject.Inject

class CharacterRepository @Inject constructor(
    private val apiService: ApiService,
    private val characterDatabase: CharacterDatabase
) {
    @OptIn(ExperimentalPagingApi::class)
    fun getCharacters() = Pager(
        config = PagingConfig(pageSize = 10, maxSize = 100),
        remoteMediator = CharacterRemoteMediator(apiService, characterDatabase),
        pagingSourceFactory = { characterDatabase.characterDao().getCharacters() }
    ).liveData

    @OptIn(ExperimentalPagingApi::class)
    fun getCharactersSortedByName() = Pager(
        config = PagingConfig(pageSize = 10, maxSize = 100),
        remoteMediator = CharacterRemoteMediator(apiService, characterDatabase),
        pagingSourceFactory = { characterDatabase.characterDao().getCharactersSortedByName() }
    ).liveData

    @OptIn(ExperimentalPagingApi::class)
    fun getCharactersSortedByHeight() = Pager(
        config = PagingConfig(pageSize = 10, maxSize = 100),
        remoteMediator = CharacterRemoteMediator(apiService, characterDatabase),
        pagingSourceFactory = { characterDatabase.characterDao().getCharactersSortedByHeight() }
    ).liveData

    @OptIn(ExperimentalPagingApi::class)
    fun getCharactersSortedByMass() = Pager(
        config = PagingConfig(pageSize = 10, maxSize = 100),
        remoteMediator = CharacterRemoteMediator(apiService, characterDatabase),
        pagingSourceFactory = { characterDatabase.characterDao().getCharactersSortedByMass() }
    ).liveData

    @OptIn(ExperimentalPagingApi::class)
    fun getCharactersSortedByCreated() = Pager(
        config = PagingConfig(pageSize = 10, maxSize = 100),
        remoteMediator = CharacterRemoteMediator(apiService, characterDatabase),
        pagingSourceFactory = { characterDatabase.characterDao().getCharactersSortedByCreated() }
    ).liveData

    @OptIn(ExperimentalPagingApi::class)
    fun getCharactersSortedByEdited() = Pager(
        config = PagingConfig(pageSize = 10, maxSize = 100),
        remoteMediator = CharacterRemoteMediator(apiService, characterDatabase),
        pagingSourceFactory = { characterDatabase.characterDao().getCharactersSortedByEdited() }
    ).liveData

    @OptIn(ExperimentalPagingApi::class)
    fun getCharactersFilteredByHairColor(color: String) = Pager(
        config = PagingConfig(pageSize = 10, maxSize = 100),
        remoteMediator = CharacterRemoteMediator(apiService, characterDatabase),
        pagingSourceFactory = { characterDatabase.characterDao().getCharactersByHairColor(color) }
    ).liveData

    @OptIn(ExperimentalPagingApi::class)
    fun getCharactersFilteredByHeight(minHeight: String, maxHeight: String) = Pager(
        config = PagingConfig(pageSize = 10, maxSize = 100),
        remoteMediator = CharacterRemoteMediator(apiService, characterDatabase),
        pagingSourceFactory = { characterDatabase.characterDao().getCharactersFilterByHeightRange(minHeight, maxHeight) }
    ).liveData

    @OptIn(ExperimentalPagingApi::class)
    fun getCharactersFilteredByMass(minMass: String, maxMass: String) = Pager(
            config = PagingConfig(pageSize = 10, maxSize = 100),
            remoteMediator = CharacterRemoteMediator(apiService, characterDatabase),
            pagingSourceFactory = { characterDatabase.characterDao().getCharactersFilterByMassRange(minMass, maxMass) }
        ).liveData
}