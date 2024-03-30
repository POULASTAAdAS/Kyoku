package com.poulastaa.data.model.common

sealed class EndPoints(val route: String) {
    data object GetSpotifyPlaylistSong : EndPoints(route = "/api/authorised/spotifyPlaylist")

    data object StoreBDate : EndPoints(route = "/api/authorised/storeBDate")

    data object SuggestGenre : EndPoints(route = "/api/authorised/suggestGenre")
    data object StoreGenre : EndPoints(route = "/api/authorised/storeGenre")

    data object SuggestArtist : EndPoints(route = "/api/authorised/suggestArtist")
    data object GetArtistImageUrl : EndPoints(route = "/api/authorised/getArtistImage")
    data object StoreArtist : EndPoints(route = "/api/authorised/storeArtist")

    data object Home : EndPoints(route = "/api/authorised/home")

    data object Artist : EndPoints(route = "/api/authorised/artist")

    data object PlaySongMaster : EndPoints(route = "/api/authorised/playSong/master")
    data object PlaySongPlaylist : EndPoints(route = "/api/authorised/playSong/playlist")
    data object PlaySong : EndPoints(route = "/api/authorised/playSong/song")

    data object CoverImage : EndPoints(route = "/api/authorised/coverImage")

    data object UnAuthorised : EndPoints(route = "/api/unauthorised")
}