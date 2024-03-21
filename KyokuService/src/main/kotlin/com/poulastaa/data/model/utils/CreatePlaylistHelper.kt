package com.poulastaa.data.model.utils

import com.poulastaa.domain.dao.playlist.Playlist

data class CreatePlaylistHelper(
    val typeHelper: UserTypeHelper,
    val listOfSongId: List<Long>,
    val playlist: Playlist
)
