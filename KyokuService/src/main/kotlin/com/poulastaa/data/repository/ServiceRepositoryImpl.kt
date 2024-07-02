package com.poulastaa.data.repository

import com.poulastaa.data.mappers.toPlaylistDto
import com.poulastaa.data.model.PlaylistDto
import com.poulastaa.data.model.SuggestGenreDto
import com.poulastaa.domain.model.ReqUserPayload
import com.poulastaa.domain.repository.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ServiceRepositoryImpl(
    private val setupRepo: SetupRepository,
    private val kyokuRepo: DatabaseRepository,
    private val userRepo: UserRepository,
) : ServiceRepository {
    override suspend fun getSpotifyPlaylist(
        userPayload: ReqUserPayload,
        spotifyPayload: List<SpotifySongTitle>,
    ): PlaylistDto {
        val user = userRepo.getUserOnPayload(userPayload) ?: return PlaylistDto()

        val result = setupRepo.getSpotifyPlaylist(
            spotifyPayload = spotifyPayload
        )

        CoroutineScope(Dispatchers.IO).launch {
            val songIdList = result.listOfSong.map { it.resultSong.id }
            val artistIdList = result.listOfSong.map {
                it.artistList.map { artist -> artist.id }
            }.flatten()

            kyokuRepo.updateSongPointByOne(songIdList)
            kyokuRepo.updateArtistPointByOne(artistIdList)
            userRepo.createUserPlaylist(
                userId = user.id,
                userType = userPayload.userType,
                playlistId = result.id,
                songIdList = songIdList
            )
        }

        return result.toPlaylistDto()
    }

    override suspend fun updateBDate(
        userPayload: ReqUserPayload,
        date: Long,
    ): Boolean {
        val user = userRepo.getUserOnPayload(userPayload) ?: return false

        return userRepo.updateBDate(
            userId = user.id,
            bDate = date,
            userType = userPayload.userType
        )
    }

    override suspend fun getGenre(
        userPayload: ReqUserPayload,
        genreIds: List<Int>,
    ): SuggestGenreDto {
        userRepo.getUserOnPayload(userPayload) ?: return SuggestGenreDto()

        return SuggestGenreDto(
            listOgGenre = setupRepo.getGenre(genreIds)
        )
    }
}