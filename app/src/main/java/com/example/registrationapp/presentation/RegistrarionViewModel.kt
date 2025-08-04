package com.example.registrationapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.registrationapp.domain.User
import com.example.registrationapp.domain.usecases.CheckUniqueUseCase
import com.example.registrationapp.domain.usecases.CheckValidUseCase
import com.example.registrationapp.domain.usecases.RegisterUserUseCase
import com.example.registrationapp.presentation.model.RegistrationFragmentUiEvents
import com.example.registrationapp.presentation.model.RegistrationFragmentUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val checkUniqueUseCase: CheckUniqueUseCase,
    private val checkValidUseCase: CheckValidUseCase,
    private val registerUserUseCase: RegisterUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegistrationFragmentUiState())
    val uiState: StateFlow<RegistrationFragmentUiState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<RegistrationFragmentUiEvents>(replay = 0)
    val uiEvent: MutableSharedFlow<RegistrationFragmentUiEvents> = _uiEvent


    fun onEmailChanged(email: String) {
        viewModelScope.launch {
            val isEmailValid = checkValidUseCase.isEmailValid(email)
            _uiState.value = _uiState.value.copy(isEmailValid = isEmailValid)
        }
    }

    fun onUserNameChanged(userName: String) {
        viewModelScope.launch {
            val isUsernameValid = checkValidUseCase.isUsernameValid(userName)
            _uiState.value = _uiState.value.copy(isUsernameValid = isUsernameValid)
        }
    }

    fun onPasswordChanged(password: String) {
        viewModelScope.launch {
            val isPasswordValid = checkValidUseCase.isPasswordValid(password)
            _uiState.value = _uiState.value.copy(isPasswordValid = isPasswordValid)
        }
    }

    fun onConfirmPasswordChanged(password: String, confirmPassword: String) {
        viewModelScope.launch {
            _uiState.value =
                _uiState.value.copy(isConfirmPasswordValid = password == confirmPassword)
        }
    }

    fun onRegisterClicked(userName: String, email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val isUsernameUnique = checkUniqueUseCase.isUsernameUnique(userName).first()
            val isEmailUnique = checkUniqueUseCase.isEmailUnique(email).first()

            _uiState.value = _uiState.value.copy(
                isUsernameUnique = isUsernameUnique,
                isEmailUnique = isEmailUnique
            )

            if (!isUsernameUnique || !isEmailUnique) {
                _uiState.value = _uiState.value.copy(isLoading = false)
                _uiEvent.emit(
                    RegistrationFragmentUiEvents.RegistrationError("Такой пользователь уже существует")
                )
                return@launch
            }

            try {
                registerUserUseCase.registerUser(
                    User(username = userName, email = email, password = password)
                )

                _uiState.value = _uiState.value.copy(isLoading = false)
                _uiEvent.emit(RegistrationFragmentUiEvents.RegistrationSuccess)
                _uiState.value = RegistrationFragmentUiState()

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false)
                _uiEvent.emit(
                    RegistrationFragmentUiEvents.RegistrationError("Не удалось зарегистрировать пользователя")
                )
            }
        }
    }
}
