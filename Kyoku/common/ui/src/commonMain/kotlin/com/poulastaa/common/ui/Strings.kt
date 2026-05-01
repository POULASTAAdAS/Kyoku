package com.poulastaa.common.ui

import androidx.compose.runtime.Composable
import kyoku.common.ui.generated.resources.Res
import kyoku.common.ui.generated.resources.app_icon
import kyoku.common.ui.generated.resources.continue_with_google
import kyoku.common.ui.generated.resources.create_account
import kyoku.common.ui.generated.resources.dont_have_account
import kyoku.common.ui.generated.resources.email
import kyoku.common.ui.generated.resources.forgot_password
import kyoku.common.ui.generated.resources.invalid_email
import kyoku.common.ui.generated.resources.or_signin_with
import kyoku.common.ui.generated.resources.password
import kyoku.common.ui.generated.resources.password_visibility
import kyoku.common.ui.generated.resources.sign_in
import kyoku.common.ui.generated.resources.sign_in_rest
import kyoku.common.ui.generated.resources.sign_in_s
import kyoku.common.ui.generated.resources.welcome_back
import org.jetbrains.compose.resources.stringResource

val StringAppIcon: String
    @Composable
    get() = stringResource(Res.string.app_icon)

val StringWelcomeBack: String
    @Composable
    get() = stringResource(Res.string.welcome_back)

val StringSignInS: String
    @Composable
    get() = stringResource(Res.string.sign_in_s)

val StringSignInRest: String
    @Composable
    get() = stringResource(Res.string.sign_in_rest)

val StringEmail: String
    @Composable
    get() = stringResource(Res.string.email)

val StringInvalidEmail: String
    @Composable
    get() = stringResource(Res.string.invalid_email)

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
