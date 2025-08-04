package com.example.registrationapp.presentation.model

sealed class RegistrationFragmentUiEvents() {
    data class RegistrationError(val error: String) : RegistrationFragmentUiEvents()
    object RegistrationSuccess : RegistrationFragmentUiEvents()
}
