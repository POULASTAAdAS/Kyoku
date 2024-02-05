package com.poulastaa.kyoku.domain.usecase

class UseCases(
    val validateEmail: ValidateEmail,
    val validatePassword: ValidatePassword,
    val validateUserName: ValidateUserName,
    val validateConformPassword: ValidateConformPassword
)
