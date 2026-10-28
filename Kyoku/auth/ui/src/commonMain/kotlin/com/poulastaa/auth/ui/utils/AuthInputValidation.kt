package com.poulastaa.auth.ui.utils

import com.poulastaa.common.ui.design_system.stringInvalidEmailError
import com.poulastaa.common.ui.design_system.stringPasswordLengthError
import com.poulastaa.common.ui.design_system.stringPasswordLetterError
import com.poulastaa.common.ui.design_system.stringPasswordNumberError
import com.poulastaa.common.ui.design_system.stringUsernameCharError
import com.poulastaa.common.ui.design_system.stringUsernameFirstFourError
import com.poulastaa.common.ui.design_system.stringUsernameLengthError
import com.poulastaa.common.ui.design_system.stringUsernameMaxLengthError

internal fun String.normalizedEmail() = trim()

internal fun String.normalizedPassword() = filterNot { it.isWhitespace() }

internal fun String.normalizedUsername() = trim()

internal suspend fun String.emailError() =
    if (EMAIL_REGEX.matches(this)) null else stringInvalidEmailError()

internal suspend fun String.usernameError() = when {
    length < MIN_USERNAME_LENGTH -> stringUsernameLengthError()
    length > MAX_USERNAME_LENGTH -> stringUsernameMaxLengthError()
    take(MIN_USERNAME_LENGTH).all { it.isEnglishLetter() }.not() -> stringUsernameFirstFourError()
    drop(MIN_USERNAME_LENGTH).all { it.isLetterOrDigit() || it == '_' || it == '.' }
        .not() -> stringUsernameCharError()

    else -> null
}

internal suspend fun String.passwordError() = when {
    length < MIN_PASSWORD_LENGTH -> stringPasswordLengthError()
    none { it.isLetter() } -> stringPasswordLetterError()
    none { it.isDigit() } -> stringPasswordNumberError()
    else -> null
}

internal suspend fun String.usernameInputError(): String? {
    if (length > MAX_USERNAME_LENGTH) return stringUsernameMaxLengthError()

    var acceptedLength = 0

    forEach { char ->
        when {
            acceptedLength < MIN_USERNAME_LENGTH && char.isEnglishLetter() -> acceptedLength++
            acceptedLength < MIN_USERNAME_LENGTH -> return stringUsernameFirstFourError()
            char.isLetterOrDigit() || char == '_' || char == '.' -> acceptedLength++
            else -> return stringUsernameCharError()
        }
    }

    return null
}

private fun Char.isEnglishLetter(): Boolean = this in 'A'..'Z' || this in 'a'..'z'

private const val MIN_USERNAME_LENGTH = 4
private const val MAX_USERNAME_LENGTH = 24
private const val MIN_PASSWORD_LENGTH = 8
private val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
