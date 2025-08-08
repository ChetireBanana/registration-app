package com.example.registrationapp.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.registrationapp.R
import com.example.registrationapp.databinding.FragmentRegistratrionBinding
import com.example.registrationapp.domain.usecases.CheckValidUseCase
import com.example.registrationapp.presentation.model.RegistrationFragmentUiEvents
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegistrationFragment() : Fragment() {

    private var _binding: FragmentRegistratrionBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RegistrationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegistratrionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        stateObserver()
        eventListener()
        clickListeners()
        editTextListeners()
    }

    private fun stateObserver() {
        lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                if ((uiState.isUsernameValid == CheckValidUseCase.OK ||
                            uiState.isUsernameValid == CheckValidUseCase.NOT_CHECKED) &&
                    uiState.isUsernameUnique
                ) {
                    binding.usernameErrorTextView.text = null
                } else if (!uiState.isUsernameUnique) {
                    binding.usernameErrorTextView.text =
                        requireContext().getString(R.string.username_not_unique)
                } else {
                    binding.usernameErrorTextView.text = uiState.isUsernameValid
                }

                if ((uiState.isEmailValid == CheckValidUseCase.OK ||
                            uiState.isEmailValid == CheckValidUseCase.NOT_CHECKED) &&
                    uiState.isEmailUnique
                ) {
                    binding.emailErrorTextView.text = null
                } else if (!uiState.isEmailUnique) {
                    binding.emailErrorTextView.text  =
                        requireContext().getString(R.string.email_not_unique)
                } else {
                    binding.emailErrorTextView.text  = uiState.isEmailValid
                }

                if ((uiState.isPasswordValid == CheckValidUseCase.OK ||
                            uiState.isPasswordValid == CheckValidUseCase.NOT_CHECKED)
                ) {
                    binding.passwordErrorTextView.text = null
                } else {
                    binding.passwordErrorTextView.text = uiState.isPasswordValid

                }

                if (!uiState.isConfirmPasswordValid) {
                    binding.confirmPasswordErrorTextView.text =
                        getString(R.string.password_not_confirmed)
                } else {
                    binding.confirmPasswordErrorTextView.text = null
                }

                if (uiState.isLoading) {
                    binding.loadingScreen.visibility = View.VISIBLE
                } else {
                    binding.loadingScreen.visibility = View.GONE
                }

                if (
                    uiState.isUsernameValid == CheckValidUseCase.OK &&
                    uiState.isEmailValid == CheckValidUseCase.OK &&
                    uiState.isPasswordValid == CheckValidUseCase.OK &&
                    uiState.isConfirmPasswordValid
                ) {
                    binding.createAccountButton.isEnabled = true
                    binding.enterAccountButton.isEnabled = true
                } else {
                    binding.createAccountButton.isEnabled = false
                    binding.enterAccountButton.isEnabled = false
                }
            }
        }
    }

    private fun eventListener() {
        lifecycleScope.launch {
            viewModel.uiEvent.collect { uiEvent ->
                when (uiEvent) {
                    is RegistrationFragmentUiEvents.RegistrationError -> {
                        Toast.makeText(requireContext(), uiEvent.error, Toast.LENGTH_SHORT).show()
                    }

                    RegistrationFragmentUiEvents.RegistrationSuccess -> {

                        binding.successScreen.visibility = View.VISIBLE
                        binding.lottieAnimation.playAnimation()

                        lifecycleScope.launch {
                            delay(2000)
                            binding.successScreen.visibility = View.GONE
                            binding.lottieAnimation.cancelAnimation()
                        }

                        binding.usernameEditText.text?.clear()
                        binding.emailEditText.text?.clear()
                        binding.passwordEditText.text?.clear()
                        binding.confirmPasswordEditText.text?.clear()
                    }
                }
            }
        }
    }

    private fun clickListeners() {
        binding.createAccountButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            viewModel.onRegisterClicked(
                userName = username,
                email = email,
                password = password
            )
        }

        binding.enterAccountButton.setOnClickListener {
            Toast.makeText(
                requireContext(),
                getString(R.string.enter_accaunt_button_clicked_toast), Toast.LENGTH_SHORT
            )
                .show()
        }

        binding.backButton.setOnClickListener {
            Toast.makeText(
                requireContext(),
                getString(R.string.back_button_clicked_toast), Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun editTextListeners() {
        binding.emailEditText.addTextChangedListener { editable ->
            val email = editable?.toString() ?: ""
            viewModel.onEmailChanged(email)
        }


        binding.usernameEditText.addTextChangedListener { editable ->
            val username = editable?.toString() ?: ""
            viewModel.onUserNameChanged(username)

        }

        binding.passwordEditText.addTextChangedListener { editable ->
            val password = editable?.toString() ?: ""
            viewModel.onPasswordChanged(password)
        }

        binding.confirmPasswordEditText.addTextChangedListener { editable ->
            val password = binding.passwordEditText.text.toString()
            val confirmPassword = editable?.toString() ?: ""
            viewModel.onConfirmPasswordChanged(password, confirmPassword)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}