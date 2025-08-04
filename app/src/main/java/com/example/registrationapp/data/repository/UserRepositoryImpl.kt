package com.example.registrationapp.data.repository

import com.example.registrationapp.data.local.db.dao.UserDao
import com.example.registrationapp.data.mapper.toDomain

import com.example.registrationapp.data.mapper.toEntity
import com.example.registrationapp.domain.User
import com.example.registrationapp.domain.repository.UserRepository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class UserRepositoryImpl(
    private val userDao: UserDao

) : UserRepository {
    override suspend fun registerUser(user: User) {
        return userDao.registerUser(user.toEntity())
    }

    override suspend fun getUserByUsername(username: String): Flow<User?> {
        return userDao.getUserByUsername(username).map{it?.toDomain()}
    }


    override suspend fun getUserByEmail(email: String): Flow<User?> {
        return userDao.getUserByEmail(email).map{it?.toDomain()}
    }
}