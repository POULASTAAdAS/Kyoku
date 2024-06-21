package com.poulastaa.auth.presentation.intro

import android.app.Activity

sealed interface IntroUiEvent {
    data object OnEmailLogInClick : IntroUiEvent
    data object OnGoogleLogInClick : IntroUiEvent
    data object OnGoogleLogInCancel : IntroUiEvent
    data class OnTokenReceive(
        val token: String,
        val activity: Activity,
    ) : IntroUiEvent
}