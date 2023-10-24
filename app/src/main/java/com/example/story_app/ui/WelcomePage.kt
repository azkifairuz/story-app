package com.example.story_app.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.story_app.R
import com.example.story_app.databinding.FragmentWelcomePageBinding

class WelcomePage : Fragment() {
    private lateinit var binding: FragmentWelcomePageBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWelcomePageBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loginBtn.setOnClickListener {
            val loginFragment = LoginPage()
            val fragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().apply {
                replace(
                    R.id.frame_container,
                    loginFragment,
                    LoginPage::class.java.simpleName
                )
                addToBackStack(null)
                commit()
            }
        }
        binding.registerBtn.setOnClickListener {
            val registerFragment = RegisterPage()
            val fragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().apply {
                replace(
                    R.id.frame_container,
                    registerFragment,
                    RegisterPage::class.java.simpleName
                )
                addToBackStack(null)
                commit()
            }
        }

        playAnimation()
    }
    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val login = ObjectAnimator
            .ofFloat(binding.loginBtn, View.ALPHA, 1f)
            .setDuration(500)
        val register = ObjectAnimator
            .ofFloat(binding.registerBtn, View.ALPHA, 1f)
            .setDuration(500)
        val desc = ObjectAnimator
            .ofFloat(binding.descriptionTextView, View.ALPHA, 1f)
            .setDuration(500)
        val title = ObjectAnimator
            .ofFloat(binding.titleTextView, View.ALPHA, 1f)
            .setDuration(500)
        AnimatorSet().apply {
            playSequentially(title,desc, login,register)
            start()
        }
    }
}