package com.poulastaa.auth.presentation.intro

sealed interface IntroUiEvent {
    data object OnEmailLogInClick : IntroUiEvent
    data object OnGoogleLogInClick : IntroUiEvent
}