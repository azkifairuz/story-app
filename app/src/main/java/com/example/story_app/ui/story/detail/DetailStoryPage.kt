package com.example.story_app.ui.story.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.load
import com.example.story_app.data.local.AuthPreference
import com.example.story_app.databinding.FragmentDetailStoryPageBinding


class DetailStoryPage : Fragment() {
    lateinit var binding: FragmentDetailStoryPageBinding
    private val viewModel: DetailStoryViewmodel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailStoryPageBinding.inflate(
            layoutInflater,
            container,
            false
        )

        val imgStory = binding.imageView
        val storyTitle = binding.tvTitle
        val storyDesc = binding.tvDesc

        ViewCompat.setTransitionName(imgStory, "image")
        ViewCompat.setTransitionName(storyTitle, "title")
        ViewCompat.setTransitionName(storyDesc, "desc")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pref = AuthPreference(requireContext())
        val token = pref.getUser().token
        viewModel.isLoading.observe(requireActivity()) { isLoading ->
            showLoading(isLoading)
        }
        val imgStory = binding.imageView
        val storyTitle = binding.tvTitle
        val storyDesc = binding.tvDesc
        viewModel.detailStory.observe(requireActivity()) { detail ->
            imgStory.load(detail?.photoUrl)
            storyTitle.text = detail?.name
            storyDesc.text = detail?.description
        }



        arguments?.getString(EXTRA_ID)?.let { viewModel.getDetailStory(token, it) }

    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_ID = "extra_id"

    }
}