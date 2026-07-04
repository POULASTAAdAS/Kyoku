package com.poulastaa.auth.ui.utils

internal fun String.normalizedEmail() = trim()

internal fun String.normalizedPassword() = filterNot { it.isWhitespace() }

internal fun String.normalizedUsername() = trim()

internal fun String.emailError() =
    if (EMAIL_REGEX.matches(this)) null else INVALID_EMAIL_ERROR

internal fun String.usernameError() = when {
    length < MIN_USERNAME_LENGTH -> USERNAME_LENGTH_ERROR
    length > MAX_USERNAME_LENGTH -> USERNAME_MAX_LENGTH_ERROR
    take(MIN_USERNAME_LENGTH).all { it.isEnglishLetter() }.not() -> USERNAME_FIRST_FOUR_ERROR
    drop(MIN_USERNAME_LENGTH).all { it.isLetterOrDigit() || it == '_' || it == '.' }
        .not() -> USERNAME_CHAR_ERROR

    else -> null
}

internal fun String.passwordError() = when {
    length < MIN_PASSWORD_LENGTH -> PASSWORD_LENGTH_ERROR
    none { it.isLetter() } -> PASSWORD_LETTER_ERROR
    none { it.isDigit() } -> PASSWORD_NUMBER_ERROR
    else -> null
}

internal fun String.usernameInputError(): String? {
    if (length > MAX_USERNAME_LENGTH) return USERNAME_MAX_LENGTH_ERROR

    var acceptedLength = 0

    forEach { char ->
        when {
            acceptedLength < MIN_USERNAME_LENGTH && char.isEnglishLetter() -> acceptedLength++
            acceptedLength < MIN_USERNAME_LENGTH -> return USERNAME_FIRST_FOUR_ERROR
            char.isLetterOrDigit() || char == '_' || char == '.' -> acceptedLength++
            else -> return USERNAME_CHAR_ERROR
        }
    }

    return null
}

private fun Char.isEnglishLetter(): Boolean = this in 'A'..'Z' || this in 'a'..'z'

private const val MIN_USERNAME_LENGTH = 4
private const val MAX_USERNAME_LENGTH = 24
private const val MIN_PASSWORD_LENGTH = 8
private const val INVALID_EMAIL_ERROR = "Invalid email"
private const val USERNAME_LENGTH_ERROR = "Use 4+ characters"
private const val USERNAME_MAX_LENGTH_ERROR = "Max 24 characters"
private const val USERNAME_FIRST_FOUR_ERROR = "First 4 must be letters"
private const val USERNAME_CHAR_ERROR = "Use letters, numbers, . or _"
private const val PASSWORD_LENGTH_ERROR = "Use 8+ characters"
private const val PASSWORD_LETTER_ERROR = "Add a letter"
private const val PASSWORD_NUMBER_ERROR = "Add a number"
private val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
