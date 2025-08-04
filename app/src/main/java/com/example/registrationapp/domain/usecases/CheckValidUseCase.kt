package com.example.registrationapp.domain.usecases

import android.content.Context
import android.util.Patterns
import com.example.registrationapp.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class CheckValidUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun isUsernameValid(username: String): String {
        if (username.isEmpty()) return context.getString(R.string.username_too_short)
        if (username.length > 35) return context.getString(R.string.username_too_long)
        return OK
    }

    fun isPasswordValid(password: String): String {
        if (password.length < 5) return context.getString(R.string.password_too_short)
        if (password.length > 64) return context.getString(R.string.password_too_long)
        return OK
    }

    fun isEmailValid(email: String): String {
        if (email.length > 50) return context.getString(R.string.email_too_long)
        if (!Patterns.EMAIL_ADDRESS.matcher(email)
                .matches()
        ) return context.getString(R.string.email_invalid)
        return OK
    }


    companion object {
        const val OK = "OK"
        const val NOT_CHECKED = "NOT_CHECKED"
    }
}