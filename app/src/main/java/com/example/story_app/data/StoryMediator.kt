package com.example.story_app.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.story_app.data.database.RemoteKeys
import com.example.story_app.data.database.StoryDb
import com.example.story_app.data.response.ListStoryItem
import com.example.story_app.data.response.ListStoryItemWithMap

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator(
    private val database: StoryDb,
    private val apiService: ApiService,
) : RemoteMediator<Int, ListStoryItem>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ListStoryItem>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }

            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        return try {
            val responseData = apiService.getAllStories(
                page,
                state.config.pageSize,
                0
            ).listStory
            val myResponseData = mutableListOf<ListStoryItem>()
            val responseDataWithMap = apiService.getAllStories(
                page,
                state.config.pageSize,
                1
            ).listStory
            val myResponseDataWithMap = mutableListOf<ListStoryItemWithMap>()

            responseData.map { story ->
                story?.let {
                    ListStoryItem(
                        story.id,
                        it.photoUrl,
                        null,
                        story.name,
                        story.description,
                        story.lon,
                        story.lat
                    )
                }?.let {
                    myResponseData.add(
                        it
                    )
                }
            }

            responseDataWithMap.map { story ->
                story?.let {
                    ListStoryItemWithMap(
                        it.id,
                        story.photoUrl,
                        null,
                        story.name,
                        story.description,
                        story.lon,
                        story.lat
                    )
                }?.let {
                    myResponseDataWithMap.add(
                        it
                    )
                }
            }

            val endOfPaginationReached = myResponseData.isEmpty()
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.remoteKeysDao().deleteRemoteKeys()
                    database.storyDao().deleteAll()
                    database.storyDao().deleteAllStoryWithMap()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = myResponseData.map {
                    RemoteKeys(id = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                database.remoteKeysDao().insertAll(keys)
                database.storyDao().insertStory(myResponseData)
                database.storyDao().insertStoryWithMap(myResponseDataWithMap)
            }

            MediatorResult.Success(endOfPaginationReached)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, ListStoryItem>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            database.remoteKeysDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, ListStoryItem>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            database.remoteKeysDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, ListStoryItem>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.remoteKeysDao().getRemoteKeysId(id)
            }
        }
    }
}