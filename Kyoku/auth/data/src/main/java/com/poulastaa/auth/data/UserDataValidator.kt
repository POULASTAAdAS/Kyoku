package com.poulastaa.auth.data

import android.util.Patterns
import com.poulastaa.auth.domain.PasswordState
import com.poulastaa.auth.domain.Validator
import javax.inject.Inject

class UserDataValidator @Inject constructor() : Validator {
    override fun isValidEmail(email: String) = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    override fun validatePassword(password: String): PasswordState {
        if (password.trim().isEmpty()) return PasswordState.EMPTY
        if (password.trim().length < 4) return PasswordState.TOO_SHORT
        if (password.trim().length > 15) return PasswordState.TOO_LONG

        val regex = "^[a-zA-Z0-9]+$".toRegex()
        return if (password.trim().matches(regex)) PasswordState.VALID
        else PasswordState.INVALID
    }
}