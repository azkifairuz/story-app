package com.example.story_app.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.story_app.R
import com.example.story_app.adapter.StoryAdapter
import com.example.story_app.data.local.AuthPreference
import com.example.story_app.data.response.ListStoryItem
import com.example.story_app.databinding.FragmentStoryBinding
import com.example.story_app.viewmodel.StoryViewModel


class StoryPage : Fragment(), StoryAdapter.ToDetailCallback {
    private lateinit var binding: FragmentStoryBinding
    private lateinit var storyRv: RecyclerView
    private lateinit var arrayList: ArrayList<ListStoryItem>
    private val viewModel: StoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        storyRv = binding.rvStory
        arrayList = ArrayList()
        val adapter = StoryAdapter(arrayList)
        adapter.setToDetailCallback(this)
        storyRv.adapter = adapter
        storyRv.layoutManager = LinearLayoutManager(requireContext())
        val pref = AuthPreference(requireContext())
        val token = pref.getUser().token
        viewModel.isLoading.observe(requireActivity()) { isLoading ->
            showLoading(isLoading)
        }
        viewModel.listStory.observe(requireActivity()) { listStory ->
            arrayList.clear()
            arrayList.addAll(listStory)
            storyRv.adapter?.notifyDataSetChanged()
        }
        viewModel.getListStory("Bearer $token")
        Toast.makeText(requireContext(), token, Toast.LENGTH_SHORT).show()
        Log.e("tokeb", "token:$token ")
    }

    override fun onItemClicked(story: ListStoryItem) {

    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}