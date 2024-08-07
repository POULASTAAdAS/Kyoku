package com.poulastaa.data.mapper

import com.poulastaa.data.model.auth.response.login.ApiLogInDto
import com.poulastaa.data.model.auth.response.login.LogInDto

fun ApiLogInDto.toLoInDto() = LogInDto(
    popularSongMixPrev = this.popularSongMixPrev,
    popularSongFromYourTimePrev = this.popularSongFromYourTimePrev,
    favouriteArtistMixPrev = this.favouriteArtistMixPrev,
    dayTypeSong = this.dayTypeSong,
    popularAlbum = this.popularAlbum,
    popularArtist = this.popularArtist,
    popularArtistSong = this.popularArtistSong,

    savedPlaylist = this.savedPlaylist,
    savedAlbum = this.savedAlbum,
    savedArtist = this.savedArtist,
    favouriteSong = this.favouriteSong
)