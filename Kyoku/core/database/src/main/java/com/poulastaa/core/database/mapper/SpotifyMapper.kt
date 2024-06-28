package com.poulastaa.core.database.mapper

import com.poulastaa.core.database.entity.PlaylistEntity
import com.poulastaa.core.database.entity.SongEntity
import com.poulastaa.core.database.model.PlaylistResult
import com.poulastaa.core.domain.model.Playlist
import com.poulastaa.core.domain.model.PlaylistSong
import com.poulastaa.core.domain.model.Song

fun Song.toSongEntity() = SongEntity(
    id = id,
    coverImage = coverImage,
    title = title,
    artistName = artistName,
    releaseYear = releaseYear,
    masterPlaylistUrl = masterPlaylistUrl,
    primary = primary,
    background = background,
    onBackground = onBackground
)

fun List<Song>.toSongsEntity() = map { it.toSongEntity() }

fun Playlist.toPlaylistEntity() = PlaylistEntity(
    id = id,
    name = name
)

fun PlaylistResult.toPlaylistSong() = PlaylistSong(
    id = songId,
    coverImage = songCoverImage,
    title = songTitle,
    artist = artist
)