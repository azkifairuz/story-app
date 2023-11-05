package com.example.story_app.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.story_app.data.response.ListStoryItem
import com.example.story_app.data.response.StoryResponse

class StoryPagingSource(private val apiService: ApiService): PagingSource<Int, ListStoryItem>() {
    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            Log.d("StoryPagingSource", "Loaded data for page: $anchorPosition")
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getStories(position,params.loadSize)
            val response= responseData.listStory
            Log.d("StoryPagingSource", "Loaded data for page: $position")
            LoadResult.Page(
                data = responseData.listStory ,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = position + 1
            )

        } catch (exception: Exception) {
            Log.e("StoryPagingSource", "Error loading data: ${exception.message}")
            return LoadResult.Error(exception)
        }
    }

}