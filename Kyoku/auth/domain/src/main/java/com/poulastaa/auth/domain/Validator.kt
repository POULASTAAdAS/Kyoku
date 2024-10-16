package com.poulastaa.auth.domain

import com.poulastaa.auth.domain.model.PasswordState
import com.poulastaa.auth.domain.model.UsernameState

interface Validator {
    fun isValidEmail(email: String): Boolean
    fun validatePassword(password: String): PasswordState

    fun isValidUserName(name: String): UsernameState
}