package com.poulastaa.kyoku.domain.usecase

class AuthUseCases(
    val validateEmail: ValidateEmail,
    val validatePassword: ValidatePassword,
    val validateUserName: ValidateUserName,
    val validateConformPassword: ValidateConformPassword
)
