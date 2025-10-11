package com.poulastaa.auth.data.usercase

import android.util.Patterns
import com.poulastaa.auth.domain.AuthValidator
import com.poulastaa.auth.domain.model.PasswordStatus
import com.poulastaa.auth.domain.model.UsernameStatus
import javax.inject.Inject

class AuthFieldValidator @Inject constructor() : AuthValidator {
    override fun isValidEmail(
        email: String,
    ): Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    override fun validatePassword(password: String): PasswordStatus {
        if (password.trim().isEmpty()) return PasswordStatus.EMPTY
        if (password.trim().length < 6) return PasswordStatus.TOO_SHORT
        if (password.trim().length > 15) return PasswordStatus.TOO_LONG

        return PasswordStatus.VALID
    }

    override fun isValidUserName(name: String): UsernameStatus {
        if (name.trim().isEmpty()) return UsernameStatus.EMPTY
        if (name.trim().startsWith('_')) return UsernameStatus.INVALID_START_WITH_UNDERSCORE
        if (name.trim().length > 20) return UsernameStatus.TOO_LONG

        val regex = "^[a-zA-Z0-9_]+$".toRegex()
        if (!name.trim().matches(regex)) return UsernameStatus.INVALID

        return UsernameStatus.VALID
    }
}