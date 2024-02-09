package com.poulastaa.kyoku.data.model.api.auth.email

import kotlinx.serialization.Serializable

@Serializable
enum class ResendVerificationMailStatus {
    EMAIL_ALREADY_VERIFIED,
    VERIFICATION_MAIL_SEND,
    SOMETHING_WENT_WRONG,
    NOT_A_VALID_EMAIL
}