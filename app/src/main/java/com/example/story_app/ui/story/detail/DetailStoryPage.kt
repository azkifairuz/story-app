package com.example.story_app.ui.story.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.load
import com.example.story_app.R
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
        arguments?.getString(EXTRA_ID)?.let { viewModel.getDetailStory(token, it) }
        viewModel.detailStory.observe(requireActivity()) { detail ->
           if (detail?.error == true){
               imgStory.load(arguments?.getString(EXTRA_ID))
               storyTitle.text = arguments?.getString(EXTRA_TITLE)
               storyDesc.text = arguments?.getString(EXTRA_DESC)
           }else{
               imgStory.load(detail?.story?.photoUrl)
               storyTitle.text = detail?.story?.name
               storyDesc.text = detail?.story?.description
           }
        }

    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_ID = "extra_id"
        const val EXTRA_TITLE="extra_title"
        const val EXTRA_DESC = "extra_desc"
        const val EXTRA_PHOTO = "extra_photo"


    }
}