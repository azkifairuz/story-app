package com.example.story_app.data.database

import androidx.room.Database
import com.example.story_app.data.response.ListStoryItem


@Database(
    entities = [ListStoryItem::class, RemoteKeys::class, ListStoryItemWithMap::class],
    version = 2,
    exportSchema = false
)
class StoryDb {
}