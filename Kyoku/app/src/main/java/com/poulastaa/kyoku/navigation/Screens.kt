package com.poulastaa.kyoku.navigation

sealed class Screens(val route: String) {
    data object Auth : Screens("/auth")

    data object AuthEmailLogin : Screens("/auth/emailLogin")
    data object AuthEmailSignUp : Screens("/auth/emailSignUp")

    data object ForgotPassword : Screens("/auth/forgotPassword")

    data object GetSpotifyPlaylist : Screens("/setup/getSpotifyPlaylist")
    data object SetBirthDate : Screens("/setup/setBirthDate")
    data object SuggestGenre : Screens("/setup/suggestGenre")
    data object SuggestArtist : Screens("/setup/suggestArtist")

    // todo try to design for any type of list of song
    data object SongView : Screens("/app/songView")

    // todo try to design by taking half of songView :: use drawer
    data object Player : Screens("/app/songView/player")

    data object HomeRoot : Screens("/app/homeRoot")

    data object Home : Screens("/app/homeRoot/home")
    data object Library : Screens("/app/homeRoot/library")


    data object CreatePlaylist : Screens("/app/createPlaylist")
    data object AddArtist : Screens("/app/addArtist")
    data object AddAlbum : Screens("/app/addAlbum")

    data object Profile : Screens("/app/profile")
    data object History : Screens("/app/history")
    data object Settings : Screens("/app/settings")

    data object Search : Screens("/route/search")
}