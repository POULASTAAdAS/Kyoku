package com.poulastaa.kyoku.data.model.screens.auth.root

data class RootAuthScreenState(
    val passkeyEmail: String = "",
    val passkeyEmailSupportingText: String = "",
    val isAutoFillPasskeyEmail:Boolean = false,

    val passkeyAuthLoading: Boolean = false,

    val isPasskeyEmailError: Boolean = false,

    val googleAuthLoading: Boolean =false,
    val emailAuthLoading: Boolean = false,

    val isInternetAvailable: Boolean = false
)
