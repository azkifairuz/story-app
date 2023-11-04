package com.example.story_app.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.story_app.data.response.ListStoryItem
import com.example.story_app.data.response.ListStoryItemWithMap


@Database(
    entities = [ListStoryItem::class, RemoteKeys::class, ListStoryItemWithMap::class],
    version = 2,
    exportSchema = false
)
abstract class StoryDb: RoomDatabase() {
    abstract fun storyDao(): StoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}