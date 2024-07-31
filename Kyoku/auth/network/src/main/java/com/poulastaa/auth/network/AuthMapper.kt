package com.poulastaa.auth.network

import com.poulastaa.auth.network.res.AlbumDto
import com.poulastaa.auth.network.res.AuthDto
import com.poulastaa.auth.network.res.EmailVerificationDto
import com.poulastaa.auth.network.res.LogInDto
import com.poulastaa.auth.network.res.PlaylistDto
import com.poulastaa.auth.network.res.ResponseUserDto
import com.poulastaa.auth.network.res.SongDto
import com.poulastaa.auth.network.res.UserAuthStatusDto
import com.poulastaa.core.data.model.ArtistDto
import com.poulastaa.core.data.model.PreArtistSongDto
import com.poulastaa.core.data.model.PrevAlbumDto
import com.poulastaa.core.data.model.PrevSongDetailDto
import com.poulastaa.core.data.model.PrevSongDto
import com.poulastaa.core.domain.model.Artist
import com.poulastaa.core.domain.model.AuthData
import com.poulastaa.core.domain.model.EmailVerification
import com.poulastaa.core.domain.model.LogInData
import com.poulastaa.core.domain.model.PlaylistData
import com.poulastaa.core.domain.model.PrevAlbum
import com.poulastaa.core.domain.model.PrevArtistSong
import com.poulastaa.core.domain.model.PrevSong
import com.poulastaa.core.domain.model.PrevSongDetail
import com.poulastaa.core.domain.model.ResponseUser
import com.poulastaa.core.domain.model.Song
import com.poulastaa.core.domain.model.UserAuthStatus

fun AuthDto.toAuthData() = AuthData(
    status = this.status.toUserAuthStatus(),
    user = this.user.toResponseUser(),
    logInData = this.logInData.toLoInData()
)


fun EmailVerificationDto.toEmailVerification() = EmailVerification(
    status = this.status,
    accessToken = this.accessToken,
    refreshToken = this.refreshToken
)

private fun UserAuthStatusDto.toUserAuthStatus() = when (this) {
    UserAuthStatusDto.CREATED -> UserAuthStatus.CREATED
    UserAuthStatusDto.CONFLICT -> UserAuthStatus.CONFLICT
    UserAuthStatusDto.USER_FOUND_HOME -> UserAuthStatus.USER_FOUND_HOME
    UserAuthStatusDto.USER_FOUND_STORE_B_DATE -> UserAuthStatus.USER_FOUND_STORE_B_DATE
    UserAuthStatusDto.USER_FOUND_SET_GENRE -> UserAuthStatus.USER_FOUND_STORE_B_DATE
    UserAuthStatusDto.USER_FOUND_SET_ARTIST -> UserAuthStatus.USER_FOUND_SET_ARTIST
    UserAuthStatusDto.PASSWORD_DOES_NOT_MATCH -> UserAuthStatus.PASSWORD_DOES_NOT_MATCH
    UserAuthStatusDto.TOKEN_NOT_VALID -> UserAuthStatus.TOKEN_NOT_VALID
    UserAuthStatusDto.USER_NOT_FOUND -> UserAuthStatus.USER_NOT_FOUND
    UserAuthStatusDto.EMAIL_NOT_VALID -> UserAuthStatus.EMAIL_NOT_VALID
    UserAuthStatusDto.EMAIL_NOT_VERIFIED -> UserAuthStatus.EMAIL_NOT_VERIFIED
    UserAuthStatusDto.SOMETHING_WENT_WRONG -> UserAuthStatus.SOMETHING_WENT_WRONG
}

private fun ResponseUserDto.toResponseUser() = ResponseUser(
    email = this.email,
    userName = this.userName,
    profilePic = this.profilePic
)

private fun LogInDto.toLoInData() = LogInData(
    popularSongMixPrev = this.popularSongMixPrev.map { it.toPrevSong() },
    popularSongFromYourTimePrev = this.popularSongFromYourTimePrev.map { it.toPrevSong() },
    favouriteArtistMixPrev = this.favouriteArtistMixPrev.map { it.toPrevSong() },
    dayTypeSong = this.dayTypeSong.map { it.toPrevSong() },
    popularAlbum = this.popularAlbum.map { it.toPrevAlbum() },
    popularArtist = this.popularArtist.map { it.toArtist() },
    popularArtistSong = this.popularArtistSong.map { it.toPreArtistSong() },
    savedPlaylist = this.savedPlaylist.map { it.toPlaylistData() },
    savedAlbum = this.savedAlbum.map { it.toAlbum() },
    savedArtist = this.savedArtist.map { it.toArtist() },
)

private fun PrevSongDto.toPrevSong() = PrevSong(
    songId = this.songId,
    coverImage = this.coverImage
)

private fun PrevAlbumDto.toPrevAlbum() = PrevAlbum(
    albumId = this.albumId,
    coverImage = this.coverImage,
    name = this.name
)

private fun ArtistDto.toArtist() = Artist(
    id = this.id,
    name = this.name,
    coverImage = this.coverImage,
)

private fun PrevSongDetailDto.toPrevSongDetail() = PrevSongDetail(
    songId = this.songId,
    coverImage = this.coverImage,
    title = this.title,
    artist = this.artist
)

private fun PreArtistSongDto.toPreArtistSong() = PrevArtistSong(
    artist = this.artist.toArtist(),
    songs = this.songs.map { it.toPrevSongDetail() }
)

private fun SongDto.toSong() = Song(
    id = this.id,
    coverImage = this.coverImage,
    title = this.title,
    artistName = this.artistName,
    releaseYear = this.releaseYear.toString(),
    masterPlaylistUrl = this.masterPlaylistUrl
)

private fun PlaylistDto.toPlaylistData() = PlaylistData(
    id = this.id,
    name = this.name,
    listOfSong = this.listOfSong.map { it.toSong() }
)

private fun AlbumDto.toAlbum() = PrevAlbum(
    albumId = this.id,
    coverImage = this.coverImage,
    name = this.name
)