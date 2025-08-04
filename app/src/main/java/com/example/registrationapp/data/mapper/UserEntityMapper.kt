package com.example.registrationapp.data.mapper

import com.example.registrationapp.data.local.entity.UserEntity
import com.example.registrationapp.domain.User

fun UserEntity.toDomain(): User =
    User(id, username, email, password)

fun User.toEntity(): UserEntity =
    UserEntity(id, username, email, password)