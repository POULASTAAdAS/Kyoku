package com.poulastaa.data.repository

import com.poulastaa.data.model.db_table.user_artist.EmailUserArtistRelationTable
import com.poulastaa.data.model.db_table.user_artist.GoogleUserArtistRelationTable
import com.poulastaa.data.model.db_table.user_artist.PasskeyUserArtistRelationTable
import com.poulastaa.data.model.home.*
import com.poulastaa.data.model.setup.artist.*
import com.poulastaa.data.model.setup.genre.*
import com.poulastaa.data.model.setup.set_b_date.SetBDateResponse
import com.poulastaa.data.model.setup.spotify.HandleSpotifyPlaylistStatus
import com.poulastaa.data.model.setup.spotify.SpotifyPlaylistResponse
import com.poulastaa.data.model.setup.spotify.SpotifySong
import com.poulastaa.data.model.utils.CreatePlaylistHelper
import com.poulastaa.data.model.utils.DbUsers
import com.poulastaa.data.model.utils.UserType
import com.poulastaa.data.model.utils.UserTypeHelper
import com.poulastaa.domain.dao.user_artist.EmailUserArtistRelation
import com.poulastaa.domain.dao.user_artist.GoogleUserArtistRelation
import com.poulastaa.domain.dao.user_artist.PasskeyUserArtistRelation
import com.poulastaa.domain.repository.UserServiceRepository
import com.poulastaa.domain.repository.album.AlbumRepository
import com.poulastaa.domain.repository.aritst.ArtistRepository
import com.poulastaa.domain.repository.genre.GenreRepository
import com.poulastaa.domain.repository.playlist.PlaylistRepository
import com.poulastaa.domain.repository.song.SongRepository
import com.poulastaa.plugins.dbQuery
import com.poulastaa.utils.getAlbum
import com.poulastaa.utils.removeAlbum
import com.poulastaa.utils.songDownloaderApi.makeApiCallOnNotFoundSpotifySongs
import com.poulastaa.utils.toListOfPlaylistRow
import kotlinx.coroutines.*
import kotlinx.serialization.json.*

class UserServiceRepositoryImpl(
    private val song: SongRepository,
    private val playlist: PlaylistRepository,
    private val dbUsers: DbUsers,
    private val genre: GenreRepository,
    private val artist: ArtistRepository,
    private val album: AlbumRepository,
) : UserServiceRepository {
    override suspend fun getFoundSpotifySongs(
        json: String,
        helper: UserTypeHelper
    ): SpotifyPlaylistResponse {
        val list = extractSpotifySong(json)

        if (list.isEmpty()) return SpotifyPlaylistResponse()

        val result = song.handleSpotifyPlaylist(list)

        return when (result.status) {
            HandleSpotifyPlaylistStatus.SUCCESS -> {
                CoroutineScope(Dispatchers.IO).launch { // send to make api call
                    result.spotifySongDownloaderApiReq.makeApiCallOnNotFoundSpotifySongs()
                }

                // send to make playlist of found song for this user
                CoroutineScope(Dispatchers.IO).launch {
                    val user = dbUsers.gerDbUser(userTypeHelper = helper)

                    if (user != null) {
                        createPlaylist(
                            playlistHelper = CreatePlaylistHelper(
                                typeHelper = UserTypeHelper(
                                    userType = helper.userType,
                                    id = user.id
                                ),
                                listOfSongId = result.songIdList,
                                playlistName = result.spotifyPlaylistResponse.name
                            ),
                        )
                    }
                }

                result.spotifyPlaylistResponse // send response data back
            }

            HandleSpotifyPlaylistStatus.FAILURE -> result.spotifyPlaylistResponse
        }
    }

    override suspend fun storeBDate(
        date: Long,
        helper: UserTypeHelper
    ): SetBDateResponse {
        val response = dbUsers.storeBDate(helper, date)

        return SetBDateResponse(
            status = response
        )
    }

    override suspend fun suggestGenre(
        req: SuggestGenreReq,
        helper: UserTypeHelper
    ): SuggestGenreResponse {
        val id = dbUsers.getCountryId(helper) ?: return SuggestGenreResponse(
            status = GenreResponseStatus.FAILURE
        )

        return genre.suggestGenre(req, id)
    }

    override suspend fun storeGenre(
        req: StoreGenreReq,
        helper: UserTypeHelper
    ): StoreGenreResponse {
        val user = dbUsers.gerDbUser(helper) ?: return StoreGenreResponse(
            status = GenreResponseStatus.FAILURE
        )


        return genre.storeGenre(
            helper = UserTypeHelper(
                userType = helper.userType,
                id = user.id
            ),
            genreNameList = req.data
        )
    }

    private suspend fun createPlaylist(
        playlistHelper: CreatePlaylistHelper
    ) {
        when (playlistHelper.typeHelper.userType) {
            UserType.EMAIL_USER -> playlist.cretePlaylistForEmailUser(
                playlist = playlistHelper
                    .listOfSongId
                    .toListOfPlaylistRow(playlistHelper.typeHelper.id),
                playlistName = playlistHelper.playlistName
            )

            UserType.GOOGLE_USER -> playlist.cretePlaylistForGoogleUser(
                playlist = playlistHelper
                    .listOfSongId
                    .toListOfPlaylistRow(playlistHelper.typeHelper.id),
                playlistName = playlistHelper.playlistName
            )

            UserType.PASSKEY_USER -> playlist.cretePlaylistForPasskeyUser(
                playlist = playlistHelper
                    .listOfSongId
                    .toListOfPlaylistRow(playlistHelper.typeHelper.id),
                playlistName = playlistHelper.playlistName
            )
        }
    }

    override suspend fun suggestArtist(
        req: SuggestArtistReq,
        helper: UserTypeHelper
    ): SuggestArtistResponse {
        val id = dbUsers.getCountryId(helper) ?: return SuggestArtistResponse(
            status = ArtistResponseStatus.FAILURE
        )

        return artist.suggestArtist(req, id)
    }

    override suspend fun storeArtist(
        req: StoreArtistReq,
        helper: UserTypeHelper
    ): StoreArtistResponse {
        val user = dbUsers.gerDbUser(helper) ?: return StoreArtistResponse(
            status = ArtistResponseStatus.FAILURE
        )

        return artist.storeArtist(
            helper = UserTypeHelper(
                userType = helper.userType,
                id = user.id
            ),
            artistNameList = req.data
        )
    }


    override suspend fun generateHomeResponse(
        req: HomeReq,
        helper: UserTypeHelper
    ): HomeResponse {
        val user = dbUsers.gerDbUser(helper) ?: return HomeResponse()

        return withContext(Dispatchers.IO) {
            when (req.type) {
                HomeType.NEW_USER_REQ -> {
                    val artistIdList = getArtistIdList(helper.userType, user.id)

                    val fevArtistsMixDeferred = async {
                        artist.getArtistMixPreview(
                            helper = UserTypeHelper(
                                userType = helper.userType,
                                id = user.id
                            )
                        )
                    }
                    val albumDeferred = async { album.getResponseAlbumPreviewForNewUser(artistIdList) }
                    val artistDeferred = async {
                        artist.getResponseArtistPreviewForNewUser(
                            usedId = user.id,
                            userType = helper.userType
                        )
                    }



                    HomeResponse(
                        status = HomeResponseStatus.SUCCESS,
                        type = req.type,
                        fevArtistsMixPreview = fevArtistsMixDeferred.await(),
                        albumPreview = albumDeferred.await(),
                        artistsPreview = artistDeferred.await()
                    )
                }

                HomeType.DAILY_REFRESH_REQ -> {
                    val dailyMixDeferred = async {
                        if (req.isOldEnough)
                            song.getDailyMixPreview(
                                helper = UserTypeHelper(
                                    userType = helper.userType,
                                    id = user.id
                                )
                            )
                        else DailyMixPreview()
                    }

                    val albumDeferred = async {
                        album.getResponseAlbumPreviewForDailyRefresh(
                            userType = helper.userType,
                            userId = user.id
                        )
                    }

                    val artistDeferred = async {
                        artist.getResponseArtistPreviewDailyUser(
                            user.id,
                            helper.userType
                        )
                    }


                    HomeResponse(
                        status = HomeResponseStatus.SUCCESS,
                        type = req.type,
                        albumPreview = albumDeferred.await(),
                        dailyMixPreview = dailyMixDeferred.await(),
                        artistsPreview = artistDeferred.await()
                    )
                }

                HomeType.ALREADY_USER_REQ -> HomeResponse()// this will not occur on service api
            }
        }
    }

    private fun extractSpotifySong(json: String): List<SpotifySong> {
        val list = ArrayList<SpotifySong>()

        return try {
            val jsonElement = Json.parseToJsonElement(json)

            val itemsArray = jsonElement.jsonObject["items"]?.jsonArray

            itemsArray?.forEach { item ->
                val trackJson = item?.jsonObject?.get("track") // some items don't exist this check is important

                if (trackJson != null && trackJson is JsonObject) {
                    val spotifySong = SpotifySong()

                    item.jsonObject["track"]?.jsonObject?.get("name")
                        ?.jsonPrimitive?.contentOrNull?.let { name ->
                            if (name.isNotBlank())
                                spotifySong.title = name.removeAlbum()
                        }

                    item.jsonObject["track"]?.jsonObject?.get("album")
                        ?.jsonObject?.get("name")?.jsonPrimitive?.contentOrNull?.let {
                            if (it.isNotBlank())
                                spotifySong.album = it.getAlbum()
                        }

                    if (spotifySong.album != null || spotifySong.title != null)
                        list.add(spotifySong)
                }
            }
            list
        } catch (_: Exception) {
            emptyList()
        }
    }

    private suspend fun getArtistIdList(userType: UserType, userId: Long) = dbQuery {
        when (userType) {
            UserType.GOOGLE_USER -> {
                GoogleUserArtistRelation.find {
                    GoogleUserArtistRelationTable.userId eq userId
                }.map {
                    it.artistId
                }
            }

            UserType.EMAIL_USER -> {
                EmailUserArtistRelation.find {
                    EmailUserArtistRelationTable.userId eq userId
                }.map {
                    it.artistId
                }
            }

            UserType.PASSKEY_USER -> {
                PasskeyUserArtistRelation.find {
                    PasskeyUserArtistRelationTable.userId eq userId
                }.map {
                    it.artistId
                }
            }
        }
    }
}