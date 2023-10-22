package com.example.story_app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.story_app.data.response.ListStoryItem
import com.example.story_app.databinding.StoryCardBinding

class StoryAdapter(private val listStory: List<ListStoryItem>) :
    RecyclerView.Adapter<StoryAdapter.ListViewHolder>() {
    private lateinit var toDetailCallback: ToDetailCallback

    fun setToDetailCallback(toDetailCallback: ToDetailCallback){
        this.toDetailCallback = toDetailCallback
    }

    class ListViewHolder(itemView: StoryCardBinding):RecyclerView.ViewHolder(itemView.root) {
        val imgStory= itemView.storyImage
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
        holder.imgStory.load(
            listStory[position].photoUrl
        )
        holder.storyTitle.text = listStory[position].name
        holder.storyDesc.text = listStory[position].description
    }

    interface ToDetailCallback {
        fun onItemClicked(story:ListStoryItem)
    }
}