package com.poulastaa.auth.domain

import com.poulastaa.auth.domain.model.PasswordStatus
import com.poulastaa.auth.domain.model.UsernameStatus

interface AuthValidator {
    fun isValidEmail(email: String): Boolean
    fun validatePassword(password: String): PasswordStatus

    fun isValidUserName(name: String): UsernameStatus
}