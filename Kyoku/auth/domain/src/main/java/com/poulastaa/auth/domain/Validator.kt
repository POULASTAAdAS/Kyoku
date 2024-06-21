package com.poulastaa.auth.domain

interface Validator {
    fun isValidEmail(email: String): Boolean
    fun validatePassword(password: String): PasswordState

    fun isValidUserName(name:String): UsernameState
}