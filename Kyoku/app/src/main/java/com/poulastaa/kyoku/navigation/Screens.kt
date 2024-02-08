package com.poulastaa.kyoku.navigation

sealed class Screens(val route: String) {
    data object Auth : Screens("/auth")
    data object AuthEmailLogin : Screens("/auth/emailLogin")
    data object AuthEmailSignUp : Screens("/auth/emailSignUp")

    data object ForgotPassword : Screens("/auth/forgotPassword")

    data object GetSpotifyPlaylist : Screens("/app/getSpotifyPlaylist")

    data object Home : Screens("/app/home")
}