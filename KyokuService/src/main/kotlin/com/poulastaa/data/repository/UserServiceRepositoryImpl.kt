package com.poulastaa.data.repository

import com.poulastaa.data.model.CreatePlaylistHelper
import com.poulastaa.data.model.DbUsers
import com.poulastaa.data.model.UserType
import com.poulastaa.data.model.UserTypeHelper
import com.poulastaa.data.model.setup.genre.*
import com.poulastaa.data.model.setup.set_b_date.SetBDateResponse
import com.poulastaa.data.model.spotify.HandleSpotifyPlaylistStatus
import com.poulastaa.data.model.spotify.SpotifyPlaylistResponse
import com.poulastaa.data.model.spotify.SpotifySong
import com.poulastaa.data.repository.genre.GenreRepository
import com.poulastaa.domain.repository.UserServiceRepository
import com.poulastaa.domain.repository.playlist.PlaylistRepository
import com.poulastaa.domain.repository.song.SongRepository
import com.poulastaa.utils.Constants.COVER_IMAGE_ROOT_DIR
import com.poulastaa.utils.getAlbum
import com.poulastaa.utils.removeAlbum
import com.poulastaa.utils.songDownloaderApi.makeApiCallOnNotFoundSpotifySongs
import com.poulastaa.utils.toListOfPlaylistRow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.*
import java.io.File

class UserServiceRepositoryImpl(
    private val song: SongRepository,
    private val playlist: PlaylistRepository,
    private val dbUsers: DbUsers,
    private val genre: GenreRepository
) : UserServiceRepository {
    override suspend fun getFoundSpotifySongs(
        json: String,
        user: UserTypeHelper
    ): SpotifyPlaylistResponse {
        val list = ArrayList<SpotifySong>()

        try {
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
        } catch (_: Exception) {
        }

        val result = song.handleSpotifyPlaylist(list)

        return when (result.status) {
            HandleSpotifyPlaylistStatus.SUCCESS -> {
                CoroutineScope(Dispatchers.IO).launch { // send to make api call
                    result.spotifySongDownloaderApiReq.makeApiCallOnNotFoundSpotifySongs()
                }

                // send to make playlist of found song for this user
                CoroutineScope(Dispatchers.IO).launch {
                    createPlaylist(
                        helper = CreatePlaylistHelper(
                            user = user,
                            listOfSongId = result.songIdList
                        )
                    )
                }
                result.spotifyPlaylistResponse // send response data
            }

            HandleSpotifyPlaylistStatus.FAILURE -> result.spotifyPlaylistResponse
        }
    }

    override suspend fun getSongCover(name: String): File? = song.getCoverImage(
        path = "${COVER_IMAGE_ROOT_DIR}$name" // convert to folder path
    )

    override suspend fun storeBDate(
        date: Long,
        userType: UserType,
        id: String
    ): SetBDateResponse {
        val response = when (userType) {
            UserType.GOOGLE_USER -> {
                dbUsers.googleUser.updateBDate(date, id)
            }

            UserType.EMAIL_USER -> {
                dbUsers.emailUser.updateBDate(date, id)
            }

            UserType.PASSKEY_USER -> {
                dbUsers.passekyUser.updateBDate(date, id)
            }
        }

        return SetBDateResponse(
            status = response
        )
    }

    override suspend fun suggestGenre(
        req: SuggestGenreReq,
        userType: UserTypeHelper
    ): SuggestGenreResponse {
        val id = when (userType.userType) {
            UserType.GOOGLE_USER -> dbUsers.googleUser.getCountryId(userType.id)

            UserType.EMAIL_USER -> dbUsers.emailUser.getCountryId(userType.id)

            UserType.PASSKEY_USER -> dbUsers.passekyUser.getCountryId(userType.id)
        } ?: return SuggestGenreResponse(
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

    private suspend fun createPlaylist(helper: CreatePlaylistHelper) {
        when (helper.user.userType) {
            UserType.EMAIL_USER -> playlist.cretePlaylistForEmailUser(
                helper.listOfSongId.toListOfPlaylistRow(helper.user.id)
            )


            UserType.GOOGLE_USER ->
                playlist.cretePlaylistForGoogleUser(
                    helper.listOfSongId.toListOfPlaylistRow(helper.user.id)
                )


            UserType.PASSKEY_USER -> playlist.cretePlaylistForPasskeyUser(
                helper.listOfSongId.toListOfPlaylistRow(helper.user.id)
            )
        }
    }
}