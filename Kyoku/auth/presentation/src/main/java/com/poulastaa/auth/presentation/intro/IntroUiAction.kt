package com.poulastaa.auth.presentation.intro

sealed interface IntroUiAction {
    data object OnGoogleSignInClick : IntroUiAction
    data object OnGoogleSignInCancel : IntroUiAction
    data class OnTokenReceive(
        val token: String,
        val countryCode: String,
    ) : IntroUiAction
}