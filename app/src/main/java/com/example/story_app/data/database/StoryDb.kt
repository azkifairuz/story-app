package com.example.story_app.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.story_app.data.response.ListStoryItem


@Database(
    entities = [ListStoryItem::class],
    version = 1,
    exportSchema = false
)
abstract class StoryDb: RoomDatabase() {

    companion object {
        @Volatile
        private var INSTANCE: StoryDb? = null

        @JvmStatic
        fun getDatabase(context: Context): StoryDb {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    StoryDb::class.java, "story_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}