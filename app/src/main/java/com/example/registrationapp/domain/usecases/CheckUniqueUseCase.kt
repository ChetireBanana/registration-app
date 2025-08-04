package com.example.registrationapp.domain.usecases

import com.example.registrationapp.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CheckUniqueUseCase @Inject constructor(
    val userRepository: UserRepository
) {

    suspend fun isEmailUnique(email: String): Flow<Boolean> =
        userRepository.getUserByEmail(email).map { it == null }

    suspend fun isUsernameUnique(username: String): Flow<Boolean> =
        userRepository.getUserByUsername(username).map { it == null }
}