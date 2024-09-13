package com.poulastaa.core.database.mapper

import com.poulastaa.core.database.entity.PlayerInfoEntity
import com.poulastaa.core.database.entity.PlayerSongEntity
import com.poulastaa.core.database.entity.PlaylistEntity
import com.poulastaa.core.database.entity.SongEntity
import com.poulastaa.core.domain.PlayerInfo
import com.poulastaa.core.domain.model.PlayerSong
import com.poulastaa.core.domain.model.Playlist
import com.poulastaa.core.domain.model.Song

fun PlaylistEntity.toPlaylist() = Playlist(
    id = this.id,
    name = this.name
)

fun SongEntity.toSong() = Song(
    id = this.id,
    coverImage = this.coverImage,
    title = this.title,
    artistName = this.artistName,
    releaseYear = this.releaseYear,
    masterPlaylistUrl = this.masterPlaylistUrl,
    primary = this.primary,
    background = this.background,
    onBackground = this.onBackground
)

@JvmName("SongToPlayerSongEntity1")
fun Song.toPlayerSongEntity(isInFavourite: Boolean) = PlayerSongEntity(
    id = this.id,
    title = this.title,
    artist = this.artistName,
    coverImage = this.coverImage,
    masterPlaylistUrl = this.masterPlaylistUrl,
    releaseYear = this.releaseYear.toInt(),
    primary = this.primary,
    secondary = this.background,
    background = this.onBackground,
    isInFavourite = isInFavourite
)

@JvmName("SongEntityToPlayerSongEntity1")
fun SongEntity.toPlayerSongEntity(isInFavourite: Boolean) = PlayerSongEntity(
    id = this.id,
    title = this.title,
    artist = this.artistName,
    coverImage = this.coverImage,
    masterPlaylistUrl = this.masterPlaylistUrl,
    releaseYear = this.releaseYear.toInt(),
    primary = this.primary,
    secondary = this.background,
    background = this.onBackground,
    isInFavourite = isInFavourite
)

fun PlayerInfoEntity.toPlayerInfo() = PlayerInfo(
    id = this.id,
    type = this.type,
    isShuffledEnabled = this.isShuffledEnabled,
    repeatState = this.repeatState,
    isPlaying = this.isPlaying,
    hasNext = this.hasNext,
    hasPrev = this.hasPrev
)

fun PlayerSongEntity.toPlayerSong() = PlayerSong(
    id = this.id,
    title = this.title,
    artist = this.artist,
    coverImage = this.coverImage,
    masterPlaylistUrl = this.masterPlaylistUrl,
    isInFavourite = this.isInFavourite,
    primary = this.primary,
    secondary = this.secondary,
    background = this.background,
    releaseYear = this.releaseYear
)