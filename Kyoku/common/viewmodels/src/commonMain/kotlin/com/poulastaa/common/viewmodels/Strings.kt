package com.poulastaa.common.viewmodels

import kyoku.common.viewmodels.generated.resources.Res
import kyoku.common.viewmodels.generated.resources.invalid_email
import kyoku.common.viewmodels.generated.resources.password_length_error
import kyoku.common.viewmodels.generated.resources.password_letter_error
import kyoku.common.viewmodels.generated.resources.password_number_error
import kyoku.common.viewmodels.generated.resources.username_char_error
import kyoku.common.viewmodels.generated.resources.username_first_four_error
import kyoku.common.viewmodels.generated.resources.username_length_error
import kyoku.common.viewmodels.generated.resources.username_max_length_error
import kyoku.common.viewmodels.generated.resources.verification_mail_sent
import org.jetbrains.compose.resources.getString

// Input Validation (non-composable, resolved from viewmodels)

suspend fun stringInvalidEmailError(): String = getString(Res.string.invalid_email)

suspend fun stringUsernameLengthError(): String = getString(Res.string.username_length_error)

suspend fun stringUsernameMaxLengthError(): String = getString(Res.string.username_max_length_error)

suspend fun stringUsernameFirstFourError(): String = getString(Res.string.username_first_four_error)

suspend fun stringUsernameCharError(): String = getString(Res.string.username_char_error)

suspend fun stringPasswordLengthError(): String = getString(Res.string.password_length_error)

suspend fun stringPasswordLetterError(): String = getString(Res.string.password_letter_error)

suspend fun stringPasswordNumberError(): String = getString(Res.string.password_number_error)

// Auth (non-composable, resolved from viewmodels)
suspend fun stringVerificationMailSent(): String = getString(Res.string.verification_mail_sent)
