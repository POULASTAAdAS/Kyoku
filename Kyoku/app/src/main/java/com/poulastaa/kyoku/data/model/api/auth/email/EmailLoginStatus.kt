package com.poulastaa.kyoku.data.model.api.auth.email

enum class EmailLoginStatus {
    USER_PASS_MATCHED,
    PASSWORD_DOES_NOT_MATCH,
    EMAIL_NOT_VERIFIED,
    USER_DOES_NOT_EXISTS,
    EMAIL_NOT_VALID,
    SOMETHING_WENT_WRONG,
}