package com.poulastaa.kyoku.domain.usecase

class ValidatePassword {
    operator fun invoke(password: String): PasswordValidityType {
        return if (password.trim().isEmpty()) PasswordValidityType.TYPE_PASSWORD_FIELD_EMPTY
        else if (password.length < 4) PasswordValidityType.TYPE_PASSWORD_TO_SHORT
        else PasswordValidityType.TYPE_PASSWORD_SUCCESS
    }

    enum class PasswordValidityType {
        TYPE_PASSWORD_TO_SHORT,
        TYPE_PASSWORD_FIELD_EMPTY,
        TYPE_PASSWORD_SUCCESS
    }
}