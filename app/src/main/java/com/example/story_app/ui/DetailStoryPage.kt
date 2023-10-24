package com.example.story_app.ui

import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.viewModels
import coil.load
import com.example.story_app.data.local.AuthPreference
import com.example.story_app.databinding.FragmentDetailStoryPageBinding
import com.example.story_app.viewmodel.DetailStoryViewmodel


class DetailStoryPage : Fragment() {
    lateinit var binding: FragmentDetailStoryPageBinding
    private val viewModel: DetailStoryViewmodel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailStoryPageBinding.inflate(
            layoutInflater,
            container,
            false)
        sharedElementEnterTransition = TransitionInflater.from(context)
            .inflateTransition(android.R.transition.move)
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
        viewModel.detailStory.observe(requireActivity()) {detail->
            imgStory.load(detail?.photoUrl)
            storyTitle.text = detail?.name
            storyDesc.text = detail?.description
        }

        val transitionNameImg = arguments?.getString(EXTRA_TRANSITION_NAME_IMG)
        val transitionNameTitle = arguments?.getString(EXTRA_TRANSITION_NAME_TITLE)
        val transitionNameDesc = arguments?.getString(EXTRA_TRANSITION_NAME_DESC)

        ViewCompat.setTransitionName(imgStory, transitionNameImg)
        ViewCompat.setTransitionName(storyTitle, transitionNameTitle)
        ViewCompat.setTransitionName(storyDesc, transitionNameDesc)
        arguments?.getString(EXTRA_ID)?.let { viewModel.getDetailStory(token, it) }


    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_ID = "extra_id"
        const val EXTRA_TRANSITION_NAME_IMG = "extra_transition_name_img"
        const val EXTRA_TRANSITION_NAME_TITLE = "extra_transition_name_title"
        const val EXTRA_TRANSITION_NAME_DESC = "extra_transition_name_desc"
    }
}