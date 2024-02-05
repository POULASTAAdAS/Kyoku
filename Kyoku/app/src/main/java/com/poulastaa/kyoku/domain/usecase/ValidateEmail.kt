package com.poulastaa.kyoku.domain.usecase

import android.util.Patterns

class ValidateEmail {
    operator fun invoke(email: String): EmailVerificationType {
        return if (email.trim().isEmpty()) EmailVerificationType.TYPE_EMAIL_FIELD_EMPTY
        else if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) EmailVerificationType.TYPE_EMAIL_SUCCESS
        else EmailVerificationType.TYPE_INVALID_EMAIL
    }


    enum class EmailVerificationType {
        TYPE_EMAIL_FIELD_EMPTY,
        TYPE_INVALID_EMAIL,
        TYPE_EMAIL_SUCCESS
    }
}