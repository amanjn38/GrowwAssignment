package com.finance.growwassignment.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.finance.growwassignment.CharacterDatabase
import com.finance.growwassignment.api.ApiService
import com.finance.growwassignment.models.CharacterRemoteKeys
import com.finance.growwassignment.models.Result

@OptIn(ExperimentalPagingApi::class)
class CharacterRemoteMediator(
    private val apiService: ApiService,
    private val characterDatabase: CharacterDatabase,
    private val ordering: String?
) : RemoteMediator<Int, Result>() {

    private val characterDao = characterDatabase.characterDao()
    private val remoteKeysDao = characterDatabase.remoteKeysDao()
    override suspend fun load(loadType: LoadType, state: PagingState<Int, Result>): MediatorResult {
        return try {
            val currentPage = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextPage?.minus(1) ?: 1
                }

                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevPage = remoteKeys?.prevPage
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    prevPage
                }

                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextPage = remoteKeys?.nextPage
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    nextPage
                }
            }

            System.out.println("testing1 " + ordering)
            System.out.println("testing12page " + currentPage)

            val response = apiService.getCharacters(currentPage)
            System.out.println("testing124 " + response)

            val endOfPaginationReached = (response.body()?.count?.div(10) ?: 0) + 1 == currentPage

            System.out.println("testingEnd " + endOfPaginationReached)


            val prevPage = if (currentPage == 1) null else currentPage - 1
            val nextPage = if (endOfPaginationReached) null else currentPage + 1

            System.out.println("testingPrev " + prevPage)

            System.out.println("testingNext " + nextPage)
            characterDatabase.withTransaction {

                if (loadType == LoadType.REFRESH) {
                    System.out.println("testingrefresh " + prevPage)

                    characterDao.deleteCharacters()
                    remoteKeysDao.clearRemoteKeys()
                }

                response.body()?.let { characterDao.addCharacters(it.results) }
                val keys = response.body()?.results?.map {
                    CharacterRemoteKeys(
                        id = it.url,
                        prevPage = prevPage,
                        nextPage = nextPage
                    )
                }

                if (keys != null) {
                    remoteKeysDao.insertAll(keys)
                }
            }

            MediatorResult.Success(endOfPaginationReached)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, Result>
    ): CharacterRemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.url?.let { it ->
                remoteKeysDao.getRemoteKeys(url = it)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, Result>
    ): CharacterRemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { it ->
                remoteKeysDao.getRemoteKeys(url = it.url)
            }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, Result>
    ): CharacterRemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { it ->
                remoteKeysDao.getRemoteKeys(url = it.url)
            }
    }

    private fun extractPageNumber(url: String?): Int {
        val regex = "page=(\\d+)".toRegex()
        val matchResult = url?.let { regex.find(it) }
        return matchResult?.groupValues?.getOrNull(1)?.toIntOrNull() ?: 1
    }
}