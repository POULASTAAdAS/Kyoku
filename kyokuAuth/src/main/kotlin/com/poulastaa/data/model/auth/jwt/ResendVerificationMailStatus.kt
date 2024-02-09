package com.poulastaa.data.model.auth.jwt

import kotlinx.serialization.Serializable

@Serializable
enum class ResendVerificationMailStatus {
    EMAIL_ALREADY_VERIFIED,
    VERIFICATION_MAIL_SEND,
    SOMETHING_WENT_WRONG,
    NOT_A_VALID_EMAIL
}