package com.example.story_app.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.story_app.R
import com.example.story_app.databinding.FragmentRegisterPageBinding
import com.example.story_app.viewmodel.AuthViewmodel

class RegisterPage : Fragment() {
    private val viewModel: AuthViewmodel by viewModels()
    private lateinit var binding: FragmentRegisterPageBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterPageBinding.inflate(layoutInflater, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setMyButtonEnable()
        validateTextField()

        registerUsers()
        viewModel.isLoading.observe(requireActivity()){isLoading->
            showLoading(isLoading)
        }

        viewModel.registerResponse.observe(requireActivity()) { callback ->
            Log.d("response msg", "${callback.error }")
            if (callback.error) {
                if (callback.message.contains("400")) {
                    Toast.makeText(requireActivity(),
                        getString(R.string.email_already_used),
                        Toast.LENGTH_SHORT).show()
                    Log.d("response msg", callback.message)
                } else {
                    Toast.makeText(requireActivity(),
                        getString(R.string.email_already_used),
                        Toast.LENGTH_SHORT).show()
                }
            } else {
                AlertDialog.Builder(requireActivity()).apply {
                    setTitle(getString(R.string.text_horee))
                    setMessage(getString(R.string.text_success_register))
                    setPositiveButton(getString(R.string.text_next)) { _, _ ->
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
                    create()
                    show()
                }
            }
        }
    }


    private fun registerUsers() {
        val name = binding.nameEditText
        val email = binding.emailEditText
        val password = binding.passwordEditText

        binding.btnRegister.setOnClickListener {
            viewModel.registerUser(
                name.text.toString().trim(),
                email.text.toString().trim(),
                password.text.toString().trim()
            )
        }
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun validateTextField() {
        setMyButtonEnable()
        binding.emailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        binding.passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        binding.nameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    private fun setMyButtonEnable() {
        val email = binding.emailEditText.text
        val isEmailValid = binding.emailEditText.isValidEmail(email.toString())

        val password = binding.passwordEditText.text
        val isPasswordValid = binding.passwordEditText.editableText.length >= 8

        val name = binding.nameEditText.text

        val result = email.toString().isNotEmpty() && password.toString()
            .isNotEmpty() && name.toString().isNotEmpty()

        val isTextFieldValid = isEmailValid && isPasswordValid
        binding.btnRegister.isEnabled = result && isTextFieldValid
    }
}
