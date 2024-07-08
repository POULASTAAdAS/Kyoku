package com.poulastaa.network.mapper

import com.poulastaa.core.data.model.ArtistDto
import com.poulastaa.core.data.model.PreArtistSongDto
import com.poulastaa.core.data.model.PrevAlbumDto
import com.poulastaa.core.data.model.PrevSongDetailDto
import com.poulastaa.core.data.model.PrevSongDto
import com.poulastaa.core.domain.model.Artist
import com.poulastaa.core.domain.model.NewHome
import com.poulastaa.core.domain.model.PrevAlbum
import com.poulastaa.core.domain.model.PrevArtistSong
import com.poulastaa.core.domain.model.PrevSong
import com.poulastaa.core.domain.model.PrevSongDetail
import com.poulastaa.network.model.NewHomeDto

private fun PrevSongDto.toPrevSong() = PrevSong(
    songId = this.songId,
    coverImage = this.coverImage
)

private fun PrevAlbumDto.toPrevAlbum() = PrevAlbum(
    albumId = this.albumId,
    name = this.name,
    coverImage = this.coverImage
)

private fun ArtistDto.toArtist() = Artist(
    id = this.id,
    name = this.name,
    coverImage = this.coverImage
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


fun NewHomeDto.toNewHome() = NewHome(
    status = status,
    popularSongMixPrev = this.popularSongMixPrev.map { it.toPrevSong() },
    popularSongFromYourTimePrev = this.popularSongFromYourTimePrev.map { it.toPrevSong() },
    favouriteArtistMixPrev = this.favouriteArtistMixPrev.map { it.toPrevSong() },
    dayTypeSong = this.dayTypeSong.map { it.toPrevSong() },
    popularAlbum = this.popularAlbum.map { it.toPrevAlbum() },
    popularArtist = this.popularArtist.map { it.toArtist() },
    popularArtistSong = this.popularArtistSong.map { it.toPreArtistSong() }
)