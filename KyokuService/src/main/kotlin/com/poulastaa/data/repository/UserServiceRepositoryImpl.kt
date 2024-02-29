package com.poulastaa.data.repository

import com.poulastaa.data.model.CreatePlaylistHelper
import com.poulastaa.data.model.DbUsers
import com.poulastaa.data.model.UserType
import com.poulastaa.data.model.UserTypeHelper
import com.poulastaa.data.model.setup.artist.*
import com.poulastaa.data.model.setup.genre.*
import com.poulastaa.data.model.setup.set_b_date.SetBDateResponse
import com.poulastaa.data.model.spotify.HandleSpotifyPlaylistStatus
import com.poulastaa.data.model.spotify.SpotifyPlaylistResponse
import com.poulastaa.data.model.spotify.SpotifySong
import com.poulastaa.domain.repository.UserServiceRepository
import com.poulastaa.domain.repository.aritst.ArtistRepository
import com.poulastaa.domain.repository.genre.GenreRepository
import com.poulastaa.domain.repository.playlist.PlaylistRepository
import com.poulastaa.domain.repository.song.SongRepository
import com.poulastaa.utils.getAlbum
import com.poulastaa.utils.removeAlbum
import com.poulastaa.utils.songDownloaderApi.makeApiCallOnNotFoundSpotifySongs
import com.poulastaa.utils.toListOfPlaylistRow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.*

class UserServiceRepositoryImpl(
    private val song: SongRepository,
    private val playlist: PlaylistRepository,
    private val dbUsers: DbUsers,
    private val genre: GenreRepository,
    private val artist: ArtistRepository
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
                                    id = user.id.toString()
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
                id = user.id.toString()
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
                    .toListOfPlaylistRow(playlistHelper.typeHelper.id.toLong()),
                playlistName = playlistHelper.playlistName
            )

            UserType.GOOGLE_USER -> playlist.cretePlaylistForGoogleUser(
                playlist = playlistHelper
                    .listOfSongId
                    .toListOfPlaylistRow(playlistHelper.typeHelper.id.toLong()),
                playlistName = playlistHelper.playlistName
            )

            UserType.PASSKEY_USER -> playlist.cretePlaylistForPasskeyUser(
                playlist = playlistHelper
                    .listOfSongId
                    .toListOfPlaylistRow(playlistHelper.typeHelper.id.toLong()),
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
                id = user.id.toString()
            ),
            artistNameList = req.data
        )
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
}