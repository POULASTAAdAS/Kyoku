package com.poulastaa.kyoku.domain.usecase

class ValidateConformPassword {
    operator fun invoke(password: String, conformPassword: String): ConformPasswordVerificationType =
        if (conformPassword.isEmpty()) ConformPasswordVerificationType.TYPE_EMPTY_PASSWORD
        else if (password.trim() == conformPassword.trim()) ConformPasswordVerificationType.TYPE_SAME_PASSWORD
        else ConformPasswordVerificationType.TYPE_DIFFERENT_PASSWORD


    enum class ConformPasswordVerificationType {
        TYPE_SAME_PASSWORD,
        TYPE_DIFFERENT_PASSWORD,
        TYPE_EMPTY_PASSWORD
    }
}