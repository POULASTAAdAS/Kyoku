package com.poulastaa.kyoku.domain.usecase

import com.poulastaa.kyoku.utils.isUserName


class ValidateUserName {
    operator fun invoke(userName: String): UsernameVerificationType {
        if (userName.trim().isEmpty()) return UsernameVerificationType.TYPE_USERNAME_FIELD_EMPTY
        if (userName.length > 25) return UsernameVerificationType.TYPE_USERNAME_TO_LONG
        userName.trim().forEach {
            if (!it.isUserName()) return UsernameVerificationType.TYPE_INVALID_USERNAME
        }
        return UsernameVerificationType.TYPE_USERNAME_SUCCESS
    }

    enum class UsernameVerificationType {
        TYPE_USERNAME_FIELD_EMPTY,
        TYPE_USERNAME_TO_LONG,
        TYPE_INVALID_USERNAME,
        TYPE_USERNAME_SUCCESS
    }
}