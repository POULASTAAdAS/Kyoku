package com.poulastaa.auth.data

import android.util.Patterns
import com.poulastaa.auth.domain.model.PasswordState
import com.poulastaa.auth.domain.model.UsernameState
import com.poulastaa.auth.domain.Validator
import javax.inject.Inject

class UserDataValidator @Inject constructor() : Validator {
    override fun isValidEmail(email: String) = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    override fun validatePassword(password: String): PasswordState {
        if (password.trim().isEmpty()) return PasswordState.EMPTY
        if (password.trim().length < 4) return PasswordState.TOO_SHORT
        if (password.trim().length > 15) return PasswordState.TOO_LONG

        return PasswordState.VALID
    }

    override fun isValidUserName(name: String): UsernameState {
        if (name.trim().isEmpty()) return UsernameState.EMPTY
        if (name.trim().startsWith('_')) return UsernameState.INVALID_START_WITH_UNDERSCORE
        if (name.trim().length > 20) return UsernameState.TOO_LONG

        val regex = "^[a-zA-Z0-9_]+$".toRegex()
        if (!name.trim().matches(regex)) return UsernameState.INVALID

        return UsernameState.VALID
    }
}