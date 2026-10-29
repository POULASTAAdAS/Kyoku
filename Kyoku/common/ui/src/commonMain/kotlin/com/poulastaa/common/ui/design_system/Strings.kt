package com.poulastaa.common.ui.design_system

import androidx.compose.runtime.Composable
import kyoku.common.ui.generated.resources.Res
import kyoku.common.ui.generated.resources.already_have_an_account
import kyoku.common.ui.generated.resources.app_icon
import kyoku.common.ui.generated.resources.back_button
import kyoku.common.ui.generated.resources.continue_with_google
import kyoku.common.ui.generated.resources.create_account
import kyoku.common.ui.generated.resources.dont_have_account
import kyoku.common.ui.generated.resources.email
import kyoku.common.ui.generated.resources.forgot_password
import kyoku.common.ui.generated.resources.get_otp
import kyoku.common.ui.generated.resources.invalid_email
import kyoku.common.ui.generated.resources.invalid_password
import kyoku.common.ui.generated.resources.invalid_username
import kyoku.common.ui.generated.resources.log_in
import kyoku.common.ui.generated.resources.login_welcome_back_message
import kyoku.common.ui.generated.resources.or_signin_with
import kyoku.common.ui.generated.resources.or_signup_with
import kyoku.common.ui.generated.resources.password
import kyoku.common.ui.generated.resources.password_length_error
import kyoku.common.ui.generated.resources.password_letter_error
import kyoku.common.ui.generated.resources.password_number_error
import kyoku.common.ui.generated.resources.password_visibility
import kyoku.common.ui.generated.resources.reset_password
import kyoku.common.ui.generated.resources.reset_password_message
import kyoku.common.ui.generated.resources.s
import kyoku.common.ui.generated.resources.sign_in
import kyoku.common.ui.generated.resources.sign_in_rest
import kyoku.common.ui.generated.resources.sign_up
import kyoku.common.ui.generated.resources.sign_up_rest
import kyoku.common.ui.generated.resources.signup_welcome_message
import kyoku.common.ui.generated.resources.username
import kyoku.common.ui.generated.resources.username_char_error
import kyoku.common.ui.generated.resources.username_first_four_error
import kyoku.common.ui.generated.resources.username_length_error
import kyoku.common.ui.generated.resources.username_max_length_error
import kyoku.common.ui.generated.resources.verification_mail_sent
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource

val StringAppIcon: String
    @Composable
    get() = stringResource(Res.string.app_icon)

val StringLogInWelcomeBackMessage: String
    @Composable
    get() = stringResource(Res.string.login_welcome_back_message)

val StringS: String
    @Composable
    get() = stringResource(Res.string.s)

val StringSignInRest: String
    @Composable
    get() = stringResource(Res.string.sign_in_rest)

val StringEmail: String
    @Composable
    get() = stringResource(Res.string.email)

val StringInvalidEmail: String
    @Composable
    get() = stringResource(Res.string.invalid_email)

val StringInvalidPassword: String
    @Composable
    get() = stringResource(Res.string.invalid_password)

val StringPassword: String
    @Composable
    get() = stringResource(Res.string.password)

val StringForgotPassword: String
    @Composable
    get() = stringResource(Res.string.forgot_password)

val StringPasswordVisibility: String
    @Composable
    get() = stringResource(Res.string.password_visibility)

val StringSignIn: String
    @Composable
    get() = stringResource(Res.string.sign_in)

val StringOrSigninWith: String
    @Composable
    get() = stringResource(Res.string.or_signin_with)

val StringContinueWithGoogle: String
    @Composable
    get() = stringResource(Res.string.continue_with_google)

val StringDontHaveAccount: String
    @Composable
    get() = stringResource(Res.string.dont_have_account)

val StringCreateAccount: String
    @Composable
    get() = stringResource(Res.string.create_account)

// Sign Up

val StringSignUpWelcomeBackMessage: String
    @Composable
    get() = stringResource(Res.string.signup_welcome_message)

val StringSignUpRest: String
    @Composable
    get() = stringResource(Res.string.sign_up_rest)

val StringUsername: String
    @Composable
    get() = stringResource(Res.string.username)

val StringInvalidUsername: String
    @Composable
    get() = stringResource(Res.string.invalid_username)

val StringAlreadyHaveAccount: String
    @Composable
    get() = stringResource(Res.string.already_have_an_account)

val StringLogIn: String
    @Composable
    get() = stringResource(Res.string.log_in)

val StringSignUp: String
    @Composable
    get() = stringResource(Res.string.sign_up)

val StringOrSignUpWith: String
    @Composable
    get() = stringResource(Res.string.or_signup_with)

// Forgot Password

val StringResetPassword: String
    @Composable
    get() = stringResource(Res.string.reset_password)

val StringResetPasswordMessage: String
    @Composable
    get() = stringResource(Res.string.reset_password_message)

val StringGetOtp: String
    @Composable
    get() = stringResource(Res.string.get_otp)

// Common

val StringBackButton: String
    @Composable
    get() = stringResource(Res.string.back_button)

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
