package com.example.story_app.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.story_app.data.response.ListStoryItem
import com.example.story_app.databinding.StoryCardBinding

class StoryAdapter(private val listStory: List<ListStoryItem>) :
    RecyclerView.Adapter<StoryAdapter.ListViewHolder>() {
    private lateinit var toDetailCallback: ToDetailCallback

    fun setToDetailCallback(toDetailCallback: ToDetailCallback) {
        this.toDetailCallback = toDetailCallback
    }

    class ListViewHolder(itemView: StoryCardBinding) : RecyclerView.ViewHolder(itemView.root) {
        val imgStory = itemView.storyImage
        val storyTitle = itemView.tvStoryTittle
        val storyDesc = itemView.tvStoryDesc


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = StoryCardBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int = listStory.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val imgStory = holder.imgStory
        val storyTitle = holder.storyTitle
        val storyDesc = holder.storyDesc

        imgStory.load(listStory[position].photoUrl)
        storyTitle.text = listStory[position].name
        storyDesc.text = listStory[position].description

        ViewCompat.setTransitionName(imgStory, "profile_${position}")
        ViewCompat.setTransitionName(storyTitle, "name_${position}")
        ViewCompat.setTransitionName(storyDesc, "description_${position}")

        holder.itemView.setOnClickListener {
            toDetailCallback.onItemClicked(listStory[position],imgStory, storyTitle, storyDesc)
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
}