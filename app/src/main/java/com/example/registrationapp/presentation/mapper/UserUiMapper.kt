package com.example.registrationapp.presentation.mapper

import com.example.registrationapp.domain.User
import com.example.registrationapp.presentation.model.UserUiModel

fun User.toUiModel(): UserUiModel =
    UserUiModel(username, email)