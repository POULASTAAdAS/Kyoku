package com.poulastaa.kyoku.playlist.service

import com.google.common.util.concurrent.FutureCallback
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.MoreExecutors
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.poulastaa.kyoku.grpc.gateway_playlist.*
import com.poulastaa.kyoku.grpc.model.*
import com.poulastaa.kyoku.grpc.playlist_user.EmptyResponse
import com.poulastaa.kyoku.grpc.playlist_user.PlaylistUserServiceGrpc
import com.poulastaa.kyoku.grpc.playlist_user.RequestSaveUserPlaylist
import com.poulastaa.kyoku.playlist.database.entity.EntityPlaylist
import com.poulastaa.kyoku.playlist.database.entity.PlaylistVisibility
import com.poulastaa.kyoku.playlist.database.repository.PlaylistDataSource
import com.poulastaa.kyoku.playlist.domain.model.internal.PlaylistResponse
import com.poulastaa.kyoku.playlist.utils.DebugUtils
import com.poulastaa.kyoku.playlist.utils.SpotifySongTitle
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.grpc.stub.StreamObserver
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.*
import jakarta.transaction.Transactional
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import net.devh.boot.grpc.server.service.GrpcService
import org.springframework.beans.factory.annotation.Value
import java.util.*
import java.util.concurrent.TimeUnit


private const val SPOTIFY_ACCESS_TOKEN_URL = "https://accounts.spotify.com/api/token"
private const val SPOTIFY_ACCESS_TOKEN_PARAM_KEY = "grant_type"
private const val SPOTIFY_ACCESS_TOKEN_PARAM_VALUE = "client_credentials"


@GrpcService
class GRPCGatewayRequestService(
    private val playlistDB: PlaylistDataSource,
    private val playlist: PlaylistDataService,

    @param:Value("\${spotify.clientId}")
    private val clientId: String,
    @param:Value("\${spotify.clientSecret}")

    private val clientSecret: String,
    private val gson: Gson,
) : GatewayPlaylistServiceGrpc.GatewayPlaylistServiceImplBase() {
    private lateinit var userServiceStub: PlaylistUserServiceGrpc.PlaylistUserServiceFutureStub
    private val userService: PlaylistUserServiceGrpc.PlaylistUserServiceFutureStub
        get() = userServiceStub.withDeadlineAfter(5, TimeUnit.SECONDS)

    @Transactional
    override fun getPlaylist(
        request: RequestGetPlaylist,
        responseObserver: StreamObserver<ResponseFullPlaylist>,
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            // extract songs from playlistId
            val spotifySongTitleList = getSongTitles(request.playlistId)
            if (spotifySongTitleList.isEmpty()) {
                responseObserver.onError(
                    Status.ABORTED.withDescription("no songs found on playlist")
                        .asRuntimeException()
                )
                return@launch
            }

            val songsDef = async { playlist.getSongByTitles(spotifySongTitleList) }

            //1. save playlist
            val dbPlaylistDef = async {
                val existingPlaylists = playlistDB.count()
                playlistDB.save(
                    EntityPlaylist(
                        name = "Playlist #${existingPlaylists + 1}",
                        description = "Enjoy your imported playlist",
                        visibility = PlaylistVisibility.PRIVATE.status,
                        totalDuration = 0, // TODO
                    )
                )
            }
            val songs = songsDef.await()
            val dbPlaylist = dbPlaylistDef.await()
            async { dbPlaylist.totalSongs = songs.size }.await()

            //2. save playlistId + songId


            //3. save userId + playlistId to user-service
            Futures.addCallback(
                userService.saveUserPlaylist(RequestSaveUserPlaylist.newBuilder().apply {
                    this.playlistId = dbPlaylist.id
                    this.user = RequestUser.newBuilder().apply {
                        this.email = request.user.email
                        this.type = request.user.type
                    }.build()
                }.build()),
                object : FutureCallback<EmptyResponse> {
                    override fun onSuccess(result: EmptyResponse?) {
                        responseObserver.onNext(
                            ResponseFullPlaylist.newBuilder().apply {
                                playlist = ResponsePlaylist.newBuilder().apply {
                                    playlistId = dbPlaylist.id
                                    name = dbPlaylist.name
                                    popularity = dbPlaylist.popularity
                                    status = when (dbPlaylist.visibility) {
                                        true -> ResponsePlaylist.ResponsePlaylistVisibilityState.PUBLIC
                                        false -> ResponsePlaylist.ResponsePlaylistVisibilityState.PRIVATE
                                    }
                                }.build()

                                addAllSongs(
                                    songs.map { song ->
                                        ResponseSong.newBuilder().apply {
                                            id = song.id
                                            title = song.title
                                            poster = song.poster
                                            masterPlaylist = song.masterPlaylist

                                            info = SongInfo.newBuilder().apply {
                                                songId = song.id
                                                releaseYear = song.info.releaseYear
                                                addAllComposers(
                                                    song.info.composer.map { composer ->
                                                        Composer.newBuilder().apply {
                                                            id = composer.id
                                                            name = composer.name
                                                            coverImage = composer.coverImage
                                                            followers = composer.followers
                                                        }.build()
                                                    }
                                                )
                                                popularity = song.info.popularity
                                            }.build()

                                            addAllArtists(
                                                song.artists.map { artist ->
                                                    Artist.newBuilder().apply {
                                                        id = artist.id
                                                        name = artist.name
                                                        coverImage = artist.coverImage
                                                        followers = artist.followers
                                                        birthDate = artist.birthDate.toString()
                                                        monthlyListeners = artist.monthlyListeners
                                                        addAllAlbums(
                                                            artist.albums.map { album ->
                                                                Album.newBuilder().apply {
                                                                    id = album.id
                                                                    name = album.name
                                                                    poster = album.poster
                                                                    addAllArtists(
                                                                        album.artists.map { albumArtist ->
                                                                            Artist.newBuilder().apply {
                                                                                id = albumArtist.id
                                                                                name = albumArtist.name
                                                                                coverImage = albumArtist.coverImage
                                                                                followers = albumArtist.followers
                                                                                birthDate =
                                                                                    albumArtist.birthDate.toString()
                                                                                monthlyListeners =
                                                                                    albumArtist.monthlyListeners
                                                                            }.build()
                                                                        }
                                                                    )
                                                                }.build()
                                                            }
                                                        )
                                                        addAllGenre(
                                                            artist.genres.map { genre ->
                                                                Genre.newBuilder().apply {
                                                                    id = genre.id
                                                                    name = genre.name
                                                                    coverImage = genre.coverImage
                                                                    popularity = genre.popularity
                                                                }.build()
                                                            }
                                                        )
                                                    }.build()
                                                }
                                            )

                                            addAllGenre(
                                                song.genre.map { genre ->
                                                    Genre.newBuilder().apply {
                                                        id = genre.id
                                                        name = genre.name
                                                        coverImage = genre.coverImage
                                                        popularity = genre.popularity
                                                    }.build()
                                                }
                                            )

                                            addAllCountry(
                                                song.country.map { country ->
                                                    Country.newBuilder().apply {
                                                        id = country.id
                                                        this.country = country.country
                                                        code = country.code
                                                    }.build()
                                                }
                                            )
                                        }.build()
                                    }
                                )
                            }.build()
                        )
                        responseObserver.onCompleted()
                    }

                    override fun onFailure(t: Throwable) {
                        val status = when (t) {
                            is StatusRuntimeException -> t.status
                            else -> Status.INTERNAL.withDescription("Failed to save user playlist: ${t.message}")
                        }

                        responseObserver.onError(status.withCause(t).asRuntimeException())
                    }
                },
                MoreExecutors.directExecutor()
            )
        }
    }

    @OptIn(InternalAPI::class)
    private suspend fun getSpotifyAccessToken(): String? {
        val client = HttpClient()

        val response = client.post(SPOTIFY_ACCESS_TOKEN_URL) {
            setBody(
                FormDataContent(Parameters.build {
                    append(
                        SPOTIFY_ACCESS_TOKEN_PARAM_KEY,
                        SPOTIFY_ACCESS_TOKEN_PARAM_VALUE
                    )
                })
            )

            headers.append("Authorization", "Basic " + "$clientId:$clientSecret".encodeBase64())
        }.bodyAsText()
        client.close()

        DebugUtils.d(response)

        val jsonObject = gson.fromJson(response, JsonObject::class.java)
        val accessToken = jsonObject.get("access_token")?.asString

        return accessToken
    }

    private suspend fun getSongTitles(spotifyPlaylistId: String): List<SpotifySongTitle> {
        val accessToken = getSpotifyAccessToken() ?: return emptyList()
        val client = HttpClient()
        val result = client.get("https://api.spotify.com/v1/playlists/$spotifyPlaylistId/tracks") {
            header("Authorization", "Bearer $accessToken")
        }
        client.close()
        DebugUtils.d(result.bodyAsText())

        val list = gson.fromJson(
            result.bodyAsText(),
            PlaylistResponse::class.java
        ).items?.mapNotNull { it.track?.name }

        return list ?: emptyList()
    }


    private fun String.encodeBase64() = Base64.getEncoder().encodeToString(this.toByteArray())
    private fun String.removeAlbumNameIfAny() = this.replace(Regex("\\(.*"), "").trim()
}