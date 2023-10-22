package com.example.story_app.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.story_app.R
import com.example.story_app.databinding.FragmentDetailStoryPageBinding

class DetailStoryPage : Fragment() {
    lateinit var binding: FragmentDetailStoryPageBinding
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


    companion object {
        const val EXTRA_ID = "extra_id"
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_DESC = "extra_desc"
    }
}