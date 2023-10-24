package com.example.story_app.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.story_app.R
import com.example.story_app.databinding.FragmentLoginPageBinding
import com.example.story_app.viewmodel.AuthViewmodel


class LoginPage : Fragment() {
    private lateinit var binding: FragmentLoginPageBinding
    private val viewModel: AuthViewmodel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginPageBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        validateTextField()

        loginUser()

        viewModel.loginResponse.observe(requireActivity()) { callback ->
            if (callback.error) {
                if (callback.message.contains("401")) {
                   AlertDialog.Builder(requireActivity()).apply {
                        setTitle(getString(R.string.text_failed))
                        setMessage(
                            "${getString(R.string.user_not_found)} or ${getString(R.string.invalid_password)} "
                        )
                        setNeutralButton(getString(R.string.text_ok)) { _, _ ->
                        }
                        create()
                        show()
                    }
                } else if (callback.message.contains("400")) {
                    Toast.makeText(
                        requireActivity(),
                        getString(R.string.user_not_found),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        requireActivity(),
                        getString(R.string.request_time_out),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                AlertDialog.Builder(requireActivity()).apply {
                    setTitle(getString(R.string.text_horee))
                    setMessage(getString(R.string.text_success_login))
                    setPositiveButton(getString(R.string.text_next)) { _, _ ->
                        val storyFragment = StoryPage()
                        val fragmentManager = parentFragmentManager
                        fragmentManager.beginTransaction().apply {
                            replace(
                                R.id.frame_container,
                                storyFragment,
                                StoryPage::class.java.simpleName
                            )
                            addToBackStack(null)
                            commit()

                        }

                    }
                    create()
                    show()
                }
            }
        }

        viewModel.isLoadingLogin.observe(requireActivity()) { isLoading ->
            showLoading(isLoading)
        }
        playAnimation()

    }

    private fun loginUser() {
        val email = binding.emailEditText
        val password = binding.passwordEditText

        binding.btnLogin.setOnClickListener {
            viewModel.loginUser(
                requireActivity(),
                email.text.toString().trim(),
                password.text.toString().trim()
            )
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        setMyButtonEnable(isLoading)
    }

    private fun validateTextField() {
        setMyButtonEnable()
        binding.emailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
        binding.passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
    }
    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val login = ObjectAnimator
            .ofFloat(binding.btnLogin, View.ALPHA, 1f)
            .setDuration(500)
        val tvEmail = ObjectAnimator
            .ofFloat(binding.emailTextView, View.ALPHA, 1f)
            .setDuration(500)
        val etEmail = ObjectAnimator
            .ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f)
            .setDuration(500)
        val tvPassword = ObjectAnimator
            .ofFloat(binding.passwordTextView, View.ALPHA, 1f)
            .setDuration(500)
        val etPassword = ObjectAnimator
            .ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f)
            .setDuration(500)
        val title = ObjectAnimator
            .ofFloat(binding.titleTextView, View.ALPHA, 1f)
            .setDuration(500)

        val emailLayout = AnimatorSet().apply {
            playTogether(tvEmail,etEmail)
        }
        val pwLayout = AnimatorSet().apply {
            playTogether(tvPassword,etPassword)
        }
        AnimatorSet().apply {
            playSequentially(title,emailLayout,pwLayout,login)
            start()
        }
    }

    private fun setMyButtonEnable(isLoading: Boolean = false) {
        val email = binding.emailEditText.text
        val isEmailValid = binding.emailEditText.isValidEmail(email.toString())

        val password = binding.passwordEditText.text
        val isPasswordValid = binding.passwordEditText.editableText.toString().length >= 8

        val isLoading = !isLoading
        val result = email.toString().isNotEmpty() && password.toString().isNotEmpty()
        val isTextFieldValid = isEmailValid && isPasswordValid
        binding.btnLogin.isEnabled = result && isTextFieldValid && isLoading
    }
}