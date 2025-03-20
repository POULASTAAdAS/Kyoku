package com.poulastaa.main.data.repository

import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.EmptyResult
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.asEmptyDataResult
import com.poulastaa.core.domain.model.DtoExploreType
import com.poulastaa.core.domain.repository.LocalHomeDatasource
import com.poulastaa.main.data.mapper.toDtoRelationSongAlbum
import com.poulastaa.main.data.mapper.toDtoRelationSongPlaylist
import com.poulastaa.main.data.mapper.toDtoSuggestedArtistSong
import com.poulastaa.main.data.mapper.toPayloadItem
import com.poulastaa.main.domain.model.DtoSuggestedArtistSong
import com.poulastaa.main.domain.model.PayloadHomeData
import com.poulastaa.main.domain.model.PayloadStaticData
import com.poulastaa.main.domain.repository.HomeRepository
import com.poulastaa.main.domain.repository.RemoteHomeDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import javax.inject.Inject

internal class OnlineFirstHomeRepository @Inject constructor(
    private val local: LocalHomeDatasource,
    private val remote: RemoteHomeDataSource,
    private val scope: CoroutineScope,
) : HomeRepository {
    override suspend fun getHome(): EmptyResult<DataError.Network> {
        val result = remote.getHome()

        if (result is Result.Success) {
            val home = result.data

            val playlistDef = scope.async {
                home.playlist.map { dto ->
                    val playlistIdDef = async { local.storePlaylist(dto.playlist) }
                    val songIdsDef = async { local.storeSong(dto.songs) }

                    playlistIdDef.await() to songIdsDef.await()
                }.also { _ ->
                    val list =
                        home.playlist.map { dto -> dto.playlist.id to dto.songs.map { it.id } }
                    local.storeRelationSongPlaylist(list.toDtoRelationSongPlaylist())
                }
            }
            val albumDef = scope.async {
                home.album.map { dto ->
                    val albumIdDef = async {
                        local.storeAlbum(dto.album).also {
                            local.storeSavedAlbum(it)
                        }
                    }
                    val songIdsDef = async { local.storeSong(dto.songs) }

                    albumIdDef.await() to songIdsDef.await()
                }.let { _ ->
                    val list = home.album.map { dto -> dto.album.id to dto.songs.map { it.id } }
                    local.stoRelationSongAlbum(list.toDtoRelationSongAlbum())
                }
            }
            val artistDef = scope.async {
                local.storeArtist(home.artist).also {
                    local.storeSavedArtist(home.artist.map { it.id })
                }
            }

            // refresh
            // explore
            (home.refresh.prevOldGem
                    + home.refresh.prevPopularSongMix
                    + home.refresh.prevPopularArtistMix).let {
                local.storePrevSong(it)
            }

            val exploreDef = scope.async {
                listOf(
                    local.storePrevExploreType(
                        type = DtoExploreType.POPULAR_YEAR_MIX,
                        data = home.refresh.prevOldGem.map { it.id }
                    ),
                    local.storePrevExploreType(
                        type = DtoExploreType.POPULAR_SONG_MIX,
                        data = home.refresh.prevPopularSongMix.map { it.id }
                    ),
                    local.storePrevExploreType(
                        type = DtoExploreType.POPULAR_ARTIST_SONG_MIX,
                        data = home.refresh.prevPopularArtistMix.map { it.id }
                    )
                )
            }
            val prevArtist = scope.async {
                local.storePrevArtist(home.refresh.suggestedArtist)
            }
            val prevAlbum = scope.async {
                local.storePrevAlbum(home.refresh.suggestedAlbum)
            }
            val prevArtistWithSong = scope.async {
                home.refresh.suggestedArtistSong.map { (artist, prevSongs) ->
                    val songId = async { local.storePrevSong(prevSongs) }
                    val artistId = async { local.storePrevArtist(artist) }

                    artistId.await() to songId.await()
                }.let { _ ->
                    val list = home.refresh.suggestedArtistSong.map { (artist, prevSongs) ->
                        artist.id to prevSongs.map { it.id }
                    }
                    local.storeRelationSuggestedArtistSong(list.toDtoSuggestedArtistSong())
                }
            }

            listOf(
                playlistDef,
                albumDef,
                artistDef,
                exploreDef,
                prevArtist,
                prevAlbum,
                prevArtistWithSong
            ).awaitAll()
        }

        return result.asEmptyDataResult()
    }

    override suspend fun loadData(): PayloadHomeData {
        val album = scope.async { local.getSavedPrevAlbum().map { it.toPayloadItem() } }
        val artist = scope.async { local.getSavedPrevArtist().map { it.toPayloadItem() } }
        val playlist = scope.async { local.getSavedPrevPlaylist().map { it.toPayloadItem() } }

        val popularSongMix =
            scope.async { local.getPrevExploreType(DtoExploreType.POPULAR_SONG_MIX) }
        val popularSongFromYourTime =
            scope.async { local.getPrevExploreType(DtoExploreType.POPULAR_YEAR_MIX) }
        val favouriteArtistMix =
            scope.async { local.getPrevExploreType(DtoExploreType.POPULAR_ARTIST_SONG_MIX) }
        val dayTypeSong = scope.async { local.getPrevExploreType(DtoExploreType.DAY_TYPE_MIX) }

        val popularAlbum = scope.async { local.getSuggestedAlbum() }
        val suggestedArtist = scope.async { local.getSuggestedArtist() }
        val popularArtistSong = scope.async {
            local.getSuggestedArtistSong().map { (artist, songs) ->
                artist to songs.map { it }
            }.mapNotNull {
                if (it.second.isEmpty() || it.second.size <= 2) null
                else DtoSuggestedArtistSong(
                    artist = it.first,
                    prevSong = it.second
                )
            }
        }

        return PayloadHomeData(
            savedItems = listOf(
                artist,
                album,
                playlist
            ).awaitAll().flatten(),
            staticData = PayloadStaticData(
                popularSongMix = popularSongMix.await(),
                popularSongFromYourTime = popularSongFromYourTime.await(),
                favouriteArtistMix = favouriteArtistMix.await(),
                dayTypeSong = dayTypeSong.await(),

                popularAlbum = popularAlbum.await(),
                suggestedArtist = suggestedArtist.await(),
                popularArtistSong = popularArtistSong.await()
            )
        )
    }
}