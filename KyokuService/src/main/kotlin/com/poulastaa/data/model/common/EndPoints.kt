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

    data object ArtistPageAlbum : EndPoints(route = "/api/authorised/artist/artistPageAlbum")
    data object ArtistPageSongs : EndPoints(route = "/api/authorised/artist/artistPageSongs")

    data object Album : EndPoints(route = "/api/authorised/album")

    data object DailyMix : EndPoints(route = "/api/authorised/dailyMix")

    data object ArtistMix : EndPoints(route = "/api/authorised/artistMix")

    data object Pinned : EndPoints(route = "/api/authorised/pinned")

    data object Item : EndPoints(route = "/api/authorised/item")

    data object PlaylistOnSongId : EndPoints("/api/authorised/playlistOnSongId")
    data object PlaylistOnAlbumId : EndPoints("/api/authorised/playlistOnAlbumId")

    data object AddSongToFavourite : EndPoints("/api/authorised/addSongToFavourite")
    data object RemoveSongFromFavourite : EndPoints("/api/authorised/removeSongFromFavourite")

    data object AddSongToPlaylist : EndPoints("/api/authorised/addSongToPlaylist")

    data object PlaySongMaster : EndPoints(route = "/api/authorised/playSong/master")
    data object PlaySongPlaylist : EndPoints(route = "/api/authorised/playSong/playlist")
    data object PlaySong : EndPoints(route = "/api/authorised/playSong/song")

    data object CoverImage : EndPoints(route = "/api/authorised/coverImage")

    data object UnAuthorised : EndPoints(route = "/api/unauthorised")
}