package com.poulastaa.core.domain.repository.song_artist

import com.poulastaa.core.domain.model.ArtistWithPopularity
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.Result

interface SongArtistRepository {
    suspend fun getArtistOnSongId(songId: Long): Result<List<ArtistWithPopularity>, DataError.Network>
}