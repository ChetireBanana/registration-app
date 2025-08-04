package com.example.registrationapp.domain.usecases

import com.example.registrationapp.domain.User
import com.example.registrationapp.domain.repository.UserRepository
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    suspend fun registerUser(user: User) {
        userRepository.registerUser(user)
    }

}