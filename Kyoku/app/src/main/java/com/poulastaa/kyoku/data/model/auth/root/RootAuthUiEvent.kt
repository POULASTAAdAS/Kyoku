package com.poulastaa.kyoku.data.model.auth.root

sealed class RootAuthUiEvent {
    data class OnPasskeyEmailEnter(val email:String): RootAuthUiEvent()
    data class OnAuthFillPasskeyEmail(val email:String): RootAuthUiEvent()

    data object OnPasskeyAuthClick: RootAuthUiEvent()

    data object OnGoogleAuthClick: RootAuthUiEvent()
    data object OnEmailAuthClick: RootAuthUiEvent()
}