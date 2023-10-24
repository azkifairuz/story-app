package com.example.story_app.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pref = AuthPreference(requireContext())
        val token = pref.getUser().token
        viewModel.isLoading.observe(requireActivity()) { isLoading ->
            showLoading(isLoading)
        }

        viewModel.detailStory.observe(requireActivity()) {detail->
            binding.imageView.load(detail?.photoUrl)
            binding.tvTitle.text = detail?.name
            binding.tvDesc.text = detail?.description
        }

        arguments?.getString(EXTRA_ID)?.let { viewModel.getDetailStory(token, it) }


    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_ID = "extra_id"
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_DESC = "extra_desc"
    }
}