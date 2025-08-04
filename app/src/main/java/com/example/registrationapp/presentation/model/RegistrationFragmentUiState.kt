package com.example.registrationapp.presentation.model

import com.example.registrationapp.domain.usecases.CheckValidUseCase

data class RegistrationFragmentUiState(
    val isLoading: Boolean = false,
    val isEmailUnique: Boolean = true,
    val isUsernameUnique: Boolean = true,
    val isUsernameValid: String = CheckValidUseCase.NOT_CHECKED,
    val isEmailValid: String = CheckValidUseCase.NOT_CHECKED,
    val isPasswordValid: String = CheckValidUseCase.NOT_CHECKED,
    val isConfirmPasswordValid: Boolean = true,
)



