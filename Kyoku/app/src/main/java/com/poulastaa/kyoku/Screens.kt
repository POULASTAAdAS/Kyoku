package com.poulastaa.kyoku

sealed class Screens(val route: String) {
    data object Intro : Screens("/intro")

    data object GetSpotifyPlaylist : Screens("/setup/getSpotifyPlaylist")
    data object SetBirthDate : Screens("/setup/setBirthDate")
    data object PicGenre : Screens("/setup/suggestGenre")
    data object PicArtist : Screens("/setup/suggestArtist")

    data object Home : Screens("/app/homeRoot/home")
}