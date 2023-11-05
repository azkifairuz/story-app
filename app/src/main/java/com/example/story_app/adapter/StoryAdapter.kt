package com.example.story_app.adapter

import android.widget.ImageView
import android.widget.TextView

//import android.app.Activity
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.core.app.ActivityOptionsCompat
//import androidx.core.view.ViewCompat
//import androidx.paging.PagingDataAdapter
//import androidx.recyclerview.widget.DiffUtil
//import androidx.recyclerview.widget.RecyclerView
//import coil.load
//import com.example.story_app.data.response.ListStoryItem
//import com.example.story_app.data.response.StoryResponse
//import com.example.story_app.databinding.StoryCardBinding
//
//class StoryAdapter(private val  listStory: List<ListStoryItem>) :
//    PagingDataAdapter<ListStoryItem, StoryAdapter.ListViewHolder>(DIFF_CALLBACK) {
//    private lateinit var toDetailCallback: ToDetailCallback
//
//    fun setToDetailCallback(toDetailCallback: ToDetailCallback) {
//        this.toDetailCallback = toDetailCallback
//    }
//
//    class ListViewHolder(itemView: StoryCardBinding) : RecyclerView.ViewHolder(itemView.root) {
//        val imgStory = itemView.storyImage
//        val storyTitle = itemView.tvStoryTittle
//        val storyDesc = itemView.tvStoryDesc
////        fun bind(data: ListStoryItem){
////
////        }
//
//
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
//        val binding = StoryCardBinding.inflate(
//            LayoutInflater.from(parent.context), parent, false
//        )
//        return ListViewHolder(binding)
//    }
//
//    override fun getItemCount(): Int = listStory.size
//
//    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
//        val imgStory = holder.imgStory
//        val storyTitle = holder.storyTitle
//        val storyDesc = holder.storyDesc
//
//        imgStory.load(listStory[position].photoUrl)
//        storyTitle.text = listStory[position].name
//        storyDesc.text = listStory[position].description
//
////        ViewCompat.setTransitionName(imgStory, "image")
////        ViewCompat.setTransitionName(storyTitle, "title")
////        ViewCompat.setTransitionName(storyDesc, "desc")
//
//        holder.itemView.setOnClickListener {
//            toDetailCallback.onItemClicked(listStory[position],imgStory, storyTitle, storyDesc)
//        }
////        val data = getItem(position)
////        if (data != null) {
////            holder.bind()
////        }
//    }
//
//    interface ToDetailCallback {
//        fun onItemClicked(
//            story: ListStoryItem,
//            imgStory: ImageView,
//            storyTitle: TextView,
//            storyDesc: TextView
//        )
//    }
//
//    companion object {
//        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
//            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
//                return oldItem == newItem
//            }
//
//            override fun areContentsTheSame(
//                oldItem: ListStoryItem,
//                newItem: ListStoryItem
//            ): Boolean {
//                return oldItem.id == newItem.id
//            }
//        }
//    }
//}

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.story_app.data.response.ListStoryItem
import com.example.story_app.databinding.StoryCardBinding

class StoryAdapter :
    PagingDataAdapter<ListStoryItem, StoryAdapter.ListViewHolder>(DIFF_CALLBACK) {

    private lateinit var toDetailCallback: ToDetailCallback

    fun setToDetailCallback(toDetailCallback: ToDetailCallback) {
        this.toDetailCallback = toDetailCallback
    }

    class ListViewHolder(binding: StoryCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val imgStory = binding.storyImage
        val storyTitle = binding.tvStoryTittle
        val storyDesc = binding.tvStoryDesc
        fun bind(data:ListStoryItem){
            imgStory.load(data.photoUrl)
            storyTitle.text = data.name
            storyDesc.text  = data.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = StoryCardBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = getItem(position)
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
        item?.let {
            val imgStory = holder.imgStory
            val storyTitle = holder.storyTitle
            val storyDesc = holder.storyDesc
            val story = it
            holder.itemView.setOnClickListener {
                toDetailCallback.onItemClicked(story, imgStory, storyTitle, storyDesc)
            }
        }
    }

    interface ToDetailCallback {
        fun onItemClicked(
            story: ListStoryItem,
            imgStory: ImageView,
            storyTitle: TextView,
            storyDesc: TextView
        )
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
