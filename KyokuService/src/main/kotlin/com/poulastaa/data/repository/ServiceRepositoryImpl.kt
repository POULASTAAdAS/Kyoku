package com.poulastaa.data.repository

import com.poulastaa.data.mappers.getYear
import com.poulastaa.data.mappers.toPlaylistDto
import com.poulastaa.data.model.PlaylistDto
import com.poulastaa.data.model.ResponseStatus
import com.poulastaa.data.model.SuggestArtistDao
import com.poulastaa.data.model.SuggestGenreDto
import com.poulastaa.data.model.home.HomeDto
import com.poulastaa.domain.model.ReqUserPayload
import com.poulastaa.domain.model.route_model.req.home.HomeReq
import com.poulastaa.domain.repository.*
import kotlinx.coroutines.*

class ServiceRepositoryImpl(
    private val setupRepo: SetupRepository,
    private val kyokuRepo: DatabaseRepository,
    private val userRepo: UserRepository,
    private val homeRepo: HomeRepository,
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

    override suspend fun storeGenre(
        userPayload: ReqUserPayload,
        genreIds: List<Int>,
    ): Boolean {
        val user = userRepo.getUserOnPayload(userPayload) ?: return false

        userRepo.storeGenre(
            userId = user.id,
            userType = user.userType,
            idList = genreIds
        )

        kyokuRepo.updateGenrePointByOne(genreIds)

        return true
    }

    override suspend fun getArtist(
        userPayload: ReqUserPayload,
        artistIds: List<Long>,
    ): SuggestArtistDao {
        userRepo.getUserOnPayload(userPayload) ?: return SuggestArtistDao()

        return SuggestArtistDao(
            listOfArtist = setupRepo.getArtist(artistIds)
        )
    }

    override suspend fun storeArtist(
        userPayload: ReqUserPayload,
        artistIds: List<Long>,
    ): Boolean {
        val user = userRepo.getUserOnPayload(userPayload) ?: return false

        userRepo.storeArtist(
            userId = user.id,
            userType = userPayload.userType,
            idList = artistIds
        )

        kyokuRepo.updateArtistPointByOne(artistIds)

        return true
    }

    override suspend fun homeReq(
        userPayload: ReqUserPayload,
        req: HomeReq,
    ): HomeDto = coroutineScope {
        val user = userRepo.getUserOnPayload(userPayload) ?: return@coroutineScope HomeDto()

        val popularSongMixDef = async { homeRepo.getPopularSongMixPrev(user.countryId) }
        val popularSongFromYourTimeDef = async {
            homeRepo.getPopularSongFromUserTimePrev(user.bDate.getYear(), user.countryId)
        }
        val favouriteArtistMixDef = async {
            homeRepo.getFavouriteArtistMixPrev(
                userId = user.id,
                userType = user.userType,
                countryId = user.countryId
            )
        }
        val dayTimeSongDef = async {
            homeRepo.getDayTypeSongPrev(
                dayType = req.dayType,
                countryId = user.countryId
            )
        }
        val poplarAlbumDef = async { homeRepo.getPopularAlbumPrev(user.countryId) }
        val poplarArtistDef = async {
            homeRepo.getPopularArtist(
                userId = user.id,
                userType = user.userType,
                countryId = user.countryId
            )
        }

        val popularArtist = poplarArtistDef.await()

        val poplarArtistSongDef = async {
            homeRepo.getPopularArtistSongPrev(
                userId = user.id,
                userType = user.userType,
                excludeArtist = popularArtist.map {
                    it.id
                },
                countryId = user.countryId
            )
        }

        HomeDto(
            status = ResponseStatus.SUCCESS,
            popularSongMixPrev = popularSongMixDef.await(),
            popularSongFromYourTimePrev = popularSongFromYourTimeDef.await(),
            favouriteArtistMixPrev = favouriteArtistMixDef.await(),
            dayTypeSong = dayTimeSongDef.await(),
            popularAlbum = poplarAlbumDef.await(),
            popularArtist = popularArtist,
            popularArtistSong = poplarArtistSongDef.await()
        )
    }
}