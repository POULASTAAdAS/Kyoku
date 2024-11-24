package com.poulastaa.play.data

import com.poulastaa.core.domain.model.ArtistWithPopularity
import com.poulastaa.core.domain.repository.song_artist.RemoteSongArtistDatasource
import com.poulastaa.core.domain.repository.song_artist.SongArtistRepository
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.Result
import javax.inject.Inject

class OnlineFirstOnlineFirstViewEditRepository @Inject constructor(
    private val remote: RemoteSongArtistDatasource,
) : SongArtistRepository {
    override suspend fun getArtistOnSongId(songId: Long): Result<List<ArtistWithPopularity>, DataError.Network> =
        remote.getArtistOnSongId(songId)
}