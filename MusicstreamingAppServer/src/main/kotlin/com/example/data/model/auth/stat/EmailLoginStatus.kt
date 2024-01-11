package com.example.data.model.auth.stat

enum class EmailLoginStatus {
    USER_PASS_MATCHED,
    PASSWORD_DOES_NOT_MATCH,
    EMAIL_NOT_VERIFIED,
    USER_DOES_NOT_EXISTS,
    EMAIL_NOT_VALID,
    SOMETHING_WENT_WRONG,
}