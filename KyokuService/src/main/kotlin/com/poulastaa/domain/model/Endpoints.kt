package com.poulastaa.domain.model

sealed class EndPoints(val route: String) {
    data object GetSpotifyPlaylistSong : EndPoints(route = "/api/authorised/spotifyPlaylist")

    data object GetCoverImage : EndPoints(route = "/api/authorised/coverImage")

    data object StoreBDate : EndPoints(route = "/api/authorised/storeBDate")

    data object SuggestGenre : EndPoints(route = "/api/authorised/suggestGenre")
    data object StoreGenre : EndPoints(route = "/api/authorised/storeGenre")

    data object SuggestArtist : EndPoints(route = "/api/authorised/suggestArtist")
    data object GetArtistImage : EndPoints(route = "/api/authorised/getArtistImage")
    data object StoreArtist : EndPoints(route = "/api/authorised/storeArtist")

    data object NewHome : EndPoints(route = "/api/authorised/newHome")

    data object PlaySongMaster : EndPoints(route = "/api/authorised/playSong/master")

    data object UnAuthorised : EndPoints(route = "/api/unauthorised")
}