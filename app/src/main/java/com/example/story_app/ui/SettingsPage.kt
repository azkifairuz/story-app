package com.example.story_app.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.story_app.R
import com.example.story_app.databinding.FragmentSettingsPageBinding
import com.example.story_app.databinding.FragmentStoryBinding

class SettingsPage : Fragment() {
    private lateinit var binding: FragmentSettingsPageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsPageBinding.inflate(inflater, container, false)
        return binding.root
    }
}