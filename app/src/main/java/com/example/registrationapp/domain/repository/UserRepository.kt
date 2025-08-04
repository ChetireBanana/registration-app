package com.example.registrationapp.domain.repository

import com.example.registrationapp.domain.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun registerUser(user: User)
    suspend fun getUserByUsername(username: String): Flow<User?>
    suspend fun getUserByEmail(email: String): Flow<User?>
}