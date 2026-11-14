package com.poulastaa.common.viewmodels

import kyoku.common.viewmodels.generated.resources.Res
import kyoku.common.viewmodels.generated.resources.empty_playlist_link
import kyoku.common.viewmodels.generated.resources.import_playlist_anyway
import kyoku.common.viewmodels.generated.resources.invalid_email
import kyoku.common.viewmodels.generated.resources.invalid_playlist_link
import kyoku.common.viewmodels.generated.resources.ok
import kyoku.common.viewmodels.generated.resources.otp_expired_error
import kyoku.common.viewmodels.generated.resources.otp_invalid_error
import kyoku.common.viewmodels.generated.resources.otp_leave_message
import kyoku.common.viewmodels.generated.resources.otp_length_error
import kyoku.common.viewmodels.generated.resources.otp_verification_success
import kyoku.common.viewmodels.generated.resources.password_length_error
import kyoku.common.viewmodels.generated.resources.password_letter_error
import kyoku.common.viewmodels.generated.resources.password_mismatch_error
import kyoku.common.viewmodels.generated.resources.password_number_error
import kyoku.common.viewmodels.generated.resources.playlist_already_imported
import kyoku.common.viewmodels.generated.resources.playlist_imported
import kyoku.common.viewmodels.generated.resources.playlist_not_found
import kyoku.common.viewmodels.generated.resources.reset_password_leave_message
import kyoku.common.viewmodels.generated.resources.reset_password_session_expired
import kyoku.common.viewmodels.generated.resources.reset_password_success
import kyoku.common.viewmodels.generated.resources.reset_password_success_save_prompt
import kyoku.common.viewmodels.generated.resources.save
import kyoku.common.viewmodels.generated.resources.save_password_prompt
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

// OTP (non-composable, resolved from viewmodels)
suspend fun stringOtpLengthError(length: Int): String = getString(Res.string.otp_length_error, length)

suspend fun stringOtpInvalidError(): String = getString(Res.string.otp_invalid_error)

suspend fun stringOtpExpiredError(): String = getString(Res.string.otp_expired_error)

suspend fun stringOtpVerificationSuccess(): String = getString(Res.string.otp_verification_success)

suspend fun stringOtpLeaveMessage(): String = getString(Res.string.otp_leave_message)

// Reset Password (non-composable, resolved from viewmodels)
suspend fun stringPasswordMismatchError(): String = getString(Res.string.password_mismatch_error)

suspend fun stringResetPasswordSuccess(): String = getString(Res.string.reset_password_success)

suspend fun stringResetPasswordSessionExpired(): String = getString(Res.string.reset_password_session_expired)

suspend fun stringResetPasswordLeaveMessage(): String = getString(Res.string.reset_password_leave_message)

// Common (non-composable, resolved from viewmodels)
suspend fun stringOk(): String = getString(Res.string.ok)

// Save Password (non-composable, resolved from viewmodels)
suspend fun stringSave(): String = getString(Res.string.save)

suspend fun stringSavePasswordPrompt(): String = getString(Res.string.save_password_prompt)

suspend fun stringResetPasswordSuccessSavePrompt(): String = getString(Res.string.reset_password_success_save_prompt)

// Import Playlist (non-composable, resolved from viewmodels)
suspend fun stringEmptyPlaylistLink(): String = getString(Res.string.empty_playlist_link)

suspend fun stringInvalidPlaylistLink(): String = getString(Res.string.invalid_playlist_link)

suspend fun stringPlaylistNotFound(): String = getString(Res.string.playlist_not_found)

suspend fun stringPlaylistAlreadyImported(): String = getString(Res.string.playlist_already_imported)

suspend fun stringImportPlaylistAnyway(): String = getString(Res.string.import_playlist_anyway)

suspend fun stringPlaylistImported(): String = getString(Res.string.playlist_imported)
