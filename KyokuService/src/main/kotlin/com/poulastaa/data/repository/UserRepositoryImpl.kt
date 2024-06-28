package com.poulastaa.data.repository

import com.poulastaa.data.mappers.toPlaylistDto
import com.poulastaa.data.model.PlaylistDto
import com.poulastaa.domain.model.ReqUserPayload
import com.poulastaa.domain.repository.DatabaseRepository
import com.poulastaa.domain.repository.SetupRepository
import com.poulastaa.domain.repository.SpotifySongTitle
import com.poulastaa.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserRepositoryImpl(
    private val setupRepository: SetupRepository,
    private val kyokuDatabase: DatabaseRepository,
) : UserRepository {
    override suspend fun getSpotifyPlaylist(
        userPayload: ReqUserPayload,
        spotifyPayload: List<SpotifySongTitle>,
    ): PlaylistDto {
        val result = setupRepository.getSpotifyPlaylist(
            spotifyPayload = spotifyPayload
        )

        CoroutineScope(Dispatchers.IO).launch {
            val songIdList = result.listOfSong.map { it.resultSong.id }
            val artistIdList = result.listOfSong.map {
                it.artistList.map { artist -> artist.id }
            }.flatten()

            kyokuDatabase.updateSongPointByOne(songIdList)
            kyokuDatabase.updateArtistPointByOne(artistIdList)
        }

        return result.toPlaylistDto()
    }
}