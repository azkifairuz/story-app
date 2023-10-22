//package com.example.story_app.viewmodel
//
//import android.content.Context
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import com.example.story_app.data.di.Injection
//import com.example.story_app.data.repo.StoryRepository
//
//class Viewmodelfactory(private val storyRepository: StoryRepository) : ViewModelProvider.Factory {
//
//    @Suppress("UNCHECKED_CAST")
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(StoryViewModel::class.java)) {
//            return StoryViewModel(storyRepository) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
//    }
//
//    companion object {
//        @Volatile
//        private var instance: Viewmodelfactory? = null
//        fun getInstance(context: Context): Viewmodelfactory =
//            instance ?: synchronized(this) {
//                instance ?: Viewmodelfactory(
//                    Injection.provideRepository(context))
//            }.also { instance = it }
//    }
//}